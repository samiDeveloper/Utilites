package bs.beansynchronizer;

import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.Driver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import lombok.val;

/** Allows run parallel at class level, parallel methods will fail. */
public class DataSourceBeanLockerTest
{
    private DataSource ds;

    @BeforeClass
    public static void beforeClass() throws SQLException
    {
        BeanSyncTestSupport.startServer();
    }

    @Before
    public void beforeMethod() throws SQLException
    {
        ds = initDatabase();
    }

    @After
    public void afterMethod()
    {
        BeanSyncTestSupport.tearDownDatabaseForThread(ds);
    }

    @Test
    public void testAcquireLockNew() throws SQLException
    {
        val expirySecs = 3;
        val tablePrefix = BeanSyncTestSupport.tablePrefixThreadSpecific();
        val clock = Clock.fixed(Instant.parse("2017-01-28T16:54:00Z"), ZoneId.of("UTC"));
        val sut = new DataSourceBeanLocker(ds, tablePrefix, clock);
        val clientId = UUID.randomUUID();
        val targetBean = BeanName.of("targetBean");

        val actualAcquired = sut.acquireLock(clientId, targetBean, expirySecs);

        Assert.assertTrue(actualAcquired);
    }

    @Test
    public void testAcquireLockRenew() throws SQLException
    {
        val expirySecs = 3;
        val tablePrefix = BeanSyncTestSupport.tablePrefixThreadSpecific();
        val clock = new MutableClockStub(Instant.parse("2017-01-28T16:54:00Z"), ZoneId.of("UTC"));
        val sut = new DataSourceBeanLocker(ds, tablePrefix, clock);
        val targetBean = BeanName.of("targetBean");

        val clientIdOne = UUID.randomUUID();
        sut.acquireLock(clientIdOne, targetBean, expirySecs);

        clock.update(clock.instant().plusSeconds(expirySecs).minusMillis(1));
        val actualAcquiredRenewed = sut.acquireLock(clientIdOne, targetBean, expirySecs);

        clock.update(clock.instant().plusSeconds(expirySecs).minusMillis(1));
        val clientTwo = UUID.randomUUID();
        val actualAcquiredSecondClient = sut.acquireLock(clientTwo, targetBean, expirySecs);

        Assert.assertTrue(actualAcquiredRenewed);
        Assert.assertFalse("Expected unable to aquire lock for other client before expiry", actualAcquiredSecondClient);
    }

    @Test
    public void testAcquireLockExpiredForOtherClient() throws SQLException
    {
        val expirySecs = 3;
        val tablePrefix = BeanSyncTestSupport.tablePrefixThreadSpecific();
        val clock = new MutableClockStub(Instant.parse("2017-01-28T16:54:00Z"), ZoneId.of("UTC"));
        val sut = new DataSourceBeanLocker(ds, tablePrefix, clock);
        val targetBean = BeanName.of("targetBean");

        val clientIdOne = UUID.randomUUID();
        sut.acquireLock(clientIdOne, targetBean, expirySecs);

        val clientTwo = UUID.randomUUID();
        clock.update(clock.instant().plusSeconds(expirySecs));
        val actualAcquired = sut.acquireLock(clientTwo, targetBean, expirySecs);
        
        Assert.assertTrue("Expected lock aquiry successful when expired", actualAcquired);
    }
    
    @Test
    public void testAcquireLockOwnedByOtherClient() throws SQLException
    {
        val expirySecs = 3;
        val tablePrefix = BeanSyncTestSupport.tablePrefixThreadSpecific();
        val clock = new MutableClockStub(Instant.parse("2017-01-28T16:54:00Z"), ZoneId.of("UTC"));
        val sut = new DataSourceBeanLocker(ds, tablePrefix, clock);
        val targetBean = BeanName.of("targetBean");

        val clientIdOne = UUID.randomUUID();
        sut.acquireLock(clientIdOne, targetBean, expirySecs);
        
        val clientTwo = UUID.randomUUID();
        clock.update(clock.instant().plusSeconds(expirySecs).minusMillis(1));
        val actualAcquired = sut.acquireLock(clientTwo, targetBean, expirySecs);
        
        Assert.assertFalse("Expected unable to acquire lock owned by another client",actualAcquired);
    }
    
    @Test
    public void testReleaseLocksForClient() throws SQLException
    {
        // prepare
        val expirySecs = 3;
        val tablePrefix = BeanSyncTestSupport.tablePrefixThreadSpecific();
        val clock = new MutableClockStub(Instant.parse("2017-01-28T16:54:00Z"), ZoneId.of("UTC"));
        val sut = new DataSourceBeanLocker(ds, tablePrefix, clock);

        val targetBeanOne = BeanName.of("targetBeanOne");
        val clientIdOne = UUID.randomUUID();
        sut.acquireLock(clientIdOne, targetBeanOne, expirySecs);
        
        val targetBeanTwo = BeanName.of("targetBeanTwo");
        val clientTwo = UUID.randomUUID();
        sut.acquireLock(clientTwo, targetBeanTwo, expirySecs);

        // exercise
        sut.releaseLocksForClient(clientIdOne); // releases targetBeanOne
        val actualAcquiredBeanOne = sut.acquireLock(UUID.randomUUID(), targetBeanOne, expirySecs);
        val actualAcquiredBeanTwo = sut.acquireLock(UUID.randomUUID(), targetBeanTwo, expirySecs);
        
        // assert
        Assert.assertTrue("Expected acquire lock successful after it was released",actualAcquiredBeanOne);
        Assert.assertFalse("Expected acquire lock fails for another client",actualAcquiredBeanTwo);
    }
    
    @Test
    public void testSize()
    {
        val expirySecs = 3;
        val tablePrefix = BeanSyncTestSupport.tablePrefixThreadSpecific();
        val clock = new MutableClockStub(Instant.parse("2017-01-28T16:54:00Z"), ZoneId.of("UTC"));
        val sut = new DataSourceBeanLocker(ds, tablePrefix, clock);
        val targetBeanOne = BeanName.of("targetBeanOne");
        val targetBeanTwo= BeanName.of("targetBeanTwo");

        val clientId = UUID.randomUUID();
        sut.acquireLock(clientId, targetBeanOne, expirySecs);
        clock.update(clock.instant().plusSeconds(expirySecs)); // targetBeanOne lock expires

        sut.acquireLock(clientId, targetBeanTwo, expirySecs); 
        
        Assert.assertEquals(1, sut.size()); // only targetBeanTwo lock present
    }
    
    @Test
    public void testReleaseAllLocks()
    {
        val expirySecs = 3;
        val tablePrefix = BeanSyncTestSupport.tablePrefixThreadSpecific();
        val clock = new MutableClockStub(Instant.parse("2017-01-28T16:54:00Z"), ZoneId.of("UTC"));
        val sut = new DataSourceBeanLocker(ds, tablePrefix, clock);
        val targetBeanOne = BeanName.of("targetBeanOne");
        
        val clientId = UUID.randomUUID();
        sut.acquireLock(clientId, targetBeanOne, expirySecs);
        sut.releaseAllLocks();
        
        Assert.assertEquals(0, sut.size());
    }
    
    private static DataSource initDatabase() throws SQLException
    {
        BasicDataSource ds = createDataSource();
        BeanSyncTestSupport.createLockTableForThread(ds);
        return ds;
    }

    private static BasicDataSource createDataSource()
    {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(Driver.class.getName());
        ds.setUsername("SA");
        ds.setPassword("");
        ds.setUrl("jdbc:h2:mem:testdb");
        return ds;
    }
}
