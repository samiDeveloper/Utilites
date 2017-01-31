package bs.beansynchronizer;

import java.net.BindException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.h2.tools.Server;

final class BeanSyncTestSupport
{

    private static final String TABLENAME = "BEAN_LOCK";

    private BeanSyncTestSupport() {
        super();
    }

    /**
     * Allows remote access to the db for debugging. Connect jdbc username is 'SA', url is
     * 'jdbc:h2:tcp://localhost:8092/mem:testdb;IFEXISTS=TRUE' Set breakpoint after server start and connect
     */
    static Server startServer() throws SQLException
    {
        // This server is JVM scoped. Start it if not yet running, and afterwards do not stop it but leave it running
        // for other test.
        Server server = Server.createTcpServer("-tcpDaemon", "-tcpPort", "8092");
        try
        {
            server.start();
        } catch (SQLException se)
        {
            // ignore if already running
            Throwable root = ExceptionUtils.getRootCause(se);
            if (!(root instanceof BindException))
            {
                throw se;
            }
        }
        return server;
    }

    static void createLockTable(DataSource ds, BeanSyncTablePrefix tablePrefix)
    {
        try (Connection con = ds.getConnection())
        {
            Statement updateStm = con.createStatement();
            String tableName = tableName(tablePrefix);
            String stm = "CREATE TABLE " + tableName + " (BEANID VARCHAR, CLIENTID VARCHAR, EXPIRYSECS INT, ISSUED_TIMESTAMP TIMESTAMP)";
            updateStm.execute(stm);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    static void createLockTableForThread(DataSource ds)
    {
        createLockTable(ds, tablePrefixThreadSpecific());
    }

    static void tearDownDatabase(DataSource ds, BeanSyncTablePrefix tablePrefix)
    {
        try (Connection con = ds.getConnection())
        {
            Statement updateStm = con.createStatement();
            String stm = "DROP TABLE " + tableName(tablePrefix);
            updateStm.execute(stm);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    static void tearDownDatabaseForThread(DataSource ds)
    {
        tearDownDatabase(ds, tablePrefixThreadSpecific());
    }

    private static String tableName(BeanSyncTablePrefix prefix)
    {
        return prefix + TABLENAME;
    }

    /** Thread specific tablename allows parallel testing */
    static BeanSyncTablePrefix tablePrefixThreadSpecific()
    {
        String threadName = Thread.currentThread()
                .getName();
        String threadNameAlfaNumCap = threadName.replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase();
        return BeanSyncTablePrefix.of(threadNameAlfaNumCap + "_");
    }

}
