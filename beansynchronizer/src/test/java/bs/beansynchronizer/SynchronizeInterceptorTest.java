package bs.beansynchronizer;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SynchronizeConfig.class, SynchronizeTestConfig.class })
public class SynchronizeInterceptorTest
{
    @Autowired
    private BarSpy barSpy;

    @Autowired
    private MapBeanLocker beanLocker;
    
    @Autowired
    private MutableClockStub clock;

    @After
    public void after()
    {
        this.barSpy.reset();
        this.beanLocker.releaseAllLocks();
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

        beanLocker.acquireLock(testClientId, targetBean); // simulates another node getting the lock

        // 1 millis before expiry
        clock.update(clock.instant().plus(Lock.EXPIRY_MINUTES, ChronoUnit.MINUTES).minusMillis(1));
        
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
        
        beanLocker.acquireLock(testClientId, targetBean);
        
        clock.update(clock.instant().plus(Lock.EXPIRY_MINUTES, ChronoUnit.MINUTES));
        
        try
        {
            barSpy.go();
        } catch (SynchronizedInvocationAborted e)
        {
            Assert.fail("Unexpected SynchronizedExecutedAborted");
        }
    }
}
