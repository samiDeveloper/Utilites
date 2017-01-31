package bs.beansynchronizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Depends on the existence of the BEAN_LOCK table.
 * 
 * <p>
 * Create SQL:
 * {@code CREATE TABLE [prefix]BEAN_LOCK (BEANID VARCHAR, CLIENTID VARCHAR, EXPIRYSECS INT, ISSUED_TIMESTAMP TIMESTAMP)}
 */
public class DataSourceBeanLocker implements BeanLocker
{
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceBeanLocker.class);

    private static final String TABLENAME = "BEAN_LOCK";

    private static final BeanSyncTablePrefix DEFAULT_TABLEPREFIX = BeanSyncTablePrefix.empty();

    private final DataSourceLockSource dataSourceLockSource;

    private final Clock clock;

    @RequiredArgsConstructor
    private static class DataSourceLockSource implements LockSource
    {
        private static final Logger LOG = LoggerFactory.getLogger(DataSourceLockSource.class);

        private final DataSource dataSource;

        private final String prefixedTableName;

        @Override
        public void put(BeanName beanName, Lock lock)
        {
            LOG.debug("put lock on " + beanName + ": " + lock);

            try (val c = dataSource.getConnection())
            {
                c.setAutoCommit(false);
                DataSourceLockSource.deleteLockFor(beanName, prefixedTableName, c);

                DataSourceLockSource.insertLock(beanName, lock, prefixedTableName, c);
                c.commit();
            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        private static void deleteLockFor(BeanName beanName, String tableName, Connection c) throws SQLException
        {
            LOG.debug("deleteLock");
            
            val stm = c.prepareStatement("DELETE FROM  " + tableName + " WHERE BEANID = ?");
            
            stm.setString(1, beanName.getBeanName()
                    .toString());
            LOG.debug("execute: " + stm);
            stm.execute();
        }

        private static void insertLock(BeanName beanName, Lock lock, String tableName, Connection c) throws SQLException
        {
            LOG.debug("insertLock");

            PreparedStatement insertStm = c
                    .prepareStatement("INSERT INTO " + tableName + " (BEANID, CLIENTID, EXPIRYSECS, ISSUED_TIMESTAMP) VALUES (?, ?, ?, ?)");
            insertStm.setString(1, beanName.getBeanName()
                    .toString());
            insertStm.setString(2, lock.getClientId()
                    .toString());
            insertStm.setInt(3, lock.getExpirySecs());
            Timestamp issuedTimestamp = new Timestamp(lock.getIssuedInstant()
                    .toEpochMilli());
            insertStm.setTimestamp(4, issuedTimestamp);

            LOG.debug("execute: " + insertStm);
            insertStm.execute();
        }

        @Override
        public Optional<Lock> get(BeanName beanName)
        {
            LOG.debug("get lock for " + beanName);

            try (val c = dataSource.getConnection())
            {
                val stmStr = "SELECT BEANID, CLIENTID, EXPIRYSECS, ISSUED_TIMESTAMP FROM  " + this.prefixedTableName + "  WHERE BEANID = ?";
                val stm = c.prepareStatement(stmStr);
                stm.setString(1, beanName.getBeanName()
                        .toString());
                LOG.debug("executeQuery: " + stm);
                val result = stm.executeQuery();
                if (!result.next())
                {
                    return Optional.empty();
                }
                val lock = rowToLock(result);

                if (result.next())
                {
                    throw new IllegalStateException(String.format("Duplicate lock for bean '%s'", beanName));
                }

                return Optional.of(lock);
            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        /** beanid, clientid, expirySecs, issuedTimstmp */
        private static Lock rowToLock(ResultSet result) throws SQLException
        {
            val clientId = result.getString(2);
            val expirySecs = result.getInt(3);
            val issuedDate = result.getTimestamp(4);
            Instant issuedInstant = issuedDate.toInstant();
            val lock = Lock.forClient(UUID.fromString(clientId), issuedInstant, expirySecs);
            return lock;
        }

        private static BeanName rowToBeanName(ResultSet result) throws SQLException
        {
            val beanNameStr = result.getString(1);
            val beanName = BeanName.of(beanNameStr);
            return beanName;
        }

        public void deleteLocksForClient(UUID clientId)
        {
            LOG.debug("deleteLocksForClient: " + clientId);
            
            try (val c = dataSource.getConnection())
            {
                val stm = c.prepareStatement("DELETE FROM  " + prefixedTableName + " WHERE CLIENTID = ?");
                stm.setString(1, clientId.toString());
                LOG.debug("execute: " + stm);
                stm.execute();
            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        public int size()
        {
            LOG.debug("size");
            
            try
            {
                val c = dataSource.getConnection();
                val stmStr = "SELECT COUNT(*) FROM  " + this.prefixedTableName;
                val stm = c.prepareStatement(stmStr);
                LOG.debug("executeQuery: " + stm);
                val result = stm.executeQuery();
                result.next();
                return result.getInt(1);

            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        public void deleteExpired(Clock clock)
        {
            LOG.debug("deleteExpired at " + clock.instant());

            try (val c = dataSource.getConnection())
            {
                // Do it in Java, not SQL
                c.setAutoCommit(false);

                val result = selectAll(prefixedTableName, c);

                val locks = rowsToBeanLockMap(result);

                Set<BeanName> toDelete = LockSupport.filterExpired(locks, clock);
                for (BeanName bean : toDelete)
                {
                    deleteLockFor(bean, this.prefixedTableName, c);
                }

                c.commit();
            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        private static HashMap<BeanName, Lock> rowsToBeanLockMap(ResultSet result) throws SQLException
        {
            val locks = new HashMap<BeanName, Lock>();
            while (result.next())
            {
                val beanName = rowToBeanName(result);
                val lock = rowToLock(result);
                locks.put(beanName, lock);
            }
            return locks;
        }

        private static ResultSet selectAll(String tableName, Connection c) throws SQLException
        {
            LOG.debug("selectAll");
            
            val stmStr = "SELECT BEANID, CLIENTID, EXPIRYSECS, ISSUED_TIMESTAMP FROM  " + tableName;
            val stm = c.prepareStatement(stmStr);
            LOG.debug("executeQuery: " + stm);
            val result = stm.executeQuery();
            return result;
        }

        public void clear()
        {
            LOG.debug("clear");
            
            try (val c = dataSource.getConnection())
            {
                val stm = c.prepareStatement("DELETE FROM  " + this.prefixedTableName);
                LOG.debug("execute: " + stm);
                stm.execute();
            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public DataSourceBeanLocker(DataSource dataSource, Clock clock) {
        this(dataSource, DEFAULT_TABLEPREFIX, clock);
    }

    public DataSourceBeanLocker(DataSource dataSource, BeanSyncTablePrefix tablePrefix, Clock clock) {
        super();
        this.dataSourceLockSource = new DataSourceLockSource(dataSource, tablePrefix + TABLENAME);
        this.clock = clock;
    }

    @Override
    public boolean acquireLock(UUID clientId, BeanName targetBean, int expirySecs)
    {
        LOG.debug("acquireLock");
        
        return LockSupport.acquireLock(dataSourceLockSource, clientId, targetBean, expirySecs, clock);
    }

    @Override
    public void releaseLocksForClient(UUID clientId)
    {
        LOG.debug("releaseLocksForClient");
        
        dataSourceLockSource.deleteLocksForClient(clientId);
    }

    @Override
    public void releaseAllLocks()
    {
        LOG.debug("releaseAllLocks");
        
        dataSourceLockSource.clear();
    }

    /** Returns the number of valid locks. Expensive because of cleanup */
    @Override
    public int size()
    {
        LOG.debug("size");
        
        cleanupExpiredLogic();
        return dataSourceLockSource.size();
    }

    @Override
    public void cleanupExpired()
    {
        LOG.debug("cleanupExpired");
        
        cleanupExpiredLogic();
    }

    private void cleanupExpiredLogic()
    {
        dataSourceLockSource.deleteExpired(clock);
    }
}
