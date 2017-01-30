package bs.beansynchronizer;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SynchronizeConfig.class, SynchronizeTestConfig.class })
public class SynchronizeInterceptorIT
{
    private static final int EXPIRY = Synchronized.DEFAULT_EXPIRY_SECONDS;
    @Autowired
    private BarSpy barSpy;

    @Autowired
    private Foo foo;

    @Autowired
    private BeanLocker beanLocker;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MutableClockStub clock;

    @Autowired
    private BeanSyncTablePrefix tablePrefix;

    @BeforeClass
    public static void beforeClass() throws SQLException
    {
        BeanSyncTestSupport.startServer();
    }

    @Before
    public void before()
    {
        BeanSyncTestSupport.createLockTable(dataSource, tablePrefix);
    }

    @After
    public void after()
    {
        this.barSpy.reset();
        this.beanLocker.releaseAllLocks();
        BeanSyncTestSupport.tearDownDatabase(dataSource, tablePrefix);
    }

    @Test
    public void testBeanIsNotLocked()
    {
        barSpy.go();

        Assert.assertTrue(barSpy.goInvoked());
    }

    /** Asserts that an invocation on a locked-out bean results in a SIA and that the bean is not invoked */
    @Test
    public void testBeanIsLocked()
    {
        BeanName targetBean = BeanName.of("bar-customname"); // corresponds to bar's beanname in SynchronizeTestConfig
        UUID testClientId = UUID.randomUUID();

        beanLocker.acquireLock(testClientId, targetBean, EXPIRY); // simulates another node
                                                                  // getting the lock

        // 1 millis before expiry
        clock.update(clock.instant()
                .plus(EXPIRY, ChronoUnit.SECONDS)
                .minusMillis(1));

        try
        {
            barSpy.go();
            Assert.fail("SynchronizedExecutedAborted exception expected");
        } catch (SynchronizedInvocationAborted e)
        {
            // expected
        }

        beanLocker.releaseAllLocks(); // release lock to be able to inspect the spy
        Assert.assertFalse(barSpy.goInvoked());
    }

    @Test
    public void testBeanLockExpired()
    {
        BeanName targetBean = BeanName.of("bar-customname");
        UUID testClientId = UUID.randomUUID();

        beanLocker.acquireLock(testClientId, targetBean, EXPIRY);

        clock.update(clock.instant()
                .plus(EXPIRY, ChronoUnit.SECONDS));

        try
        {
            barSpy.go();
        } catch (SynchronizedInvocationAborted e)
        {
            Assert.fail("Unexpected SynchronizedExecutedAborted");
        }
    }

    @Test
    public void testBeanLockExtendLock()
    {

        barSpy.go();
        clock.update(clock.instant()
                .plus(EXPIRY, ChronoUnit.SECONDS)
                .minusMillis(1));
        barSpy.go();
        clock.update(clock.instant()
                .plus(EXPIRY, ChronoUnit.SECONDS)
                .minusMillis(1));

        BeanName targetBean = BeanName.of("bar-customname");
        UUID testClientId = UUID.randomUUID();
        boolean actualOtherNodeLock = beanLocker.acquireLock(testClientId, targetBean, EXPIRY);

        Assert.assertFalse("Expected unable to acquire lock for other node, which proves that the original lock was extended", actualOtherNodeLock);
    }

    @Test
    public void testBeanLockCustomExpiry()
    {
        // Note that Synchronized.DEFAULT_EXPIRY_MINUTES > Foo.EXPIRY_MINS

        foo.start();

        clock.update(clock.instant()
                .plus(Foo.EXPIRY_SECS, ChronoUnit.SECONDS));

        BeanName targetBean = BeanName.of("foo");
        UUID testClientId = UUID.randomUUID();
        boolean actualOtherNodeLock = beanLocker.acquireLock(testClientId, targetBean, Foo.EXPIRY_SECS);

        // shows that the foo annotation's expiry is actually applied to the lock
        Assert.assertTrue("Expected able to acquire lock", actualOtherNodeLock);
    }

}
