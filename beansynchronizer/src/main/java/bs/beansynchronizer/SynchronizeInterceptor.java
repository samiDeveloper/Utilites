package bs.beansynchronizer;

import java.util.UUID;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.ExposeBeanNameAdvisors;

/**
 * Proceeds the invocation only if the bean instance has a lock. Enables synchronization of bean instances across
 * multiple JVMs (nodes). It does so by means of a lock in a database specified by the @Synchronized.
 * 
 * <p>
 * Prevents deadlocks by means of a lock expiry, at the expense of continuous availability. That means that no bean will
 * be active during periods where the active node is dead and it's lock is not yet expired.
 */
public class SynchronizeInterceptor implements MethodInterceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(SynchronizeInterceptor.class);

    private static final UUID MY_CLIENT_ID = UUID.randomUUID();

    private final BeanLocker beanLocker;

    public SynchronizeInterceptor(BeanLocker beanLocker) {
        super();
        this.beanLocker = beanLocker;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        LOG.debug("SynchronizeInterceptor hit: " + invocation.toString());

        String targetBeanName = fetchBeanName(invocation);

        int expirySeconds= figureExpirySecs(invocation);
        boolean haveLock = beanLocker.acquireLock(MY_CLIENT_ID, BeanName.of(targetBeanName), expirySeconds);

        if (haveLock)
        {
            LOG.debug("Proceed bean invocation");
            return invocation.proceed();
        } else
        {
            LOG.debug("Abort bean invocation because got no lock");
            String errMsg = String.format("Target bean name: '%s', invocation: %s", targetBeanName, invocation);
            throw new SynchronizedInvocationAborted(errMsg);
        }
    }

    private static String fetchBeanName(MethodInvocation invocation)
    {
        final String targetBeanName;
        try
        {
            targetBeanName = ExposeBeanNameAdvisors.getBeanName(invocation);
            LOG.info(String.format("Bean name: '%s'", targetBeanName));
        } catch (IllegalStateException e)
        {
            throw new IllegalStateException("Unable to fetch the invoked bean name", e);
        }
        return targetBeanName;
    }

    private int figureExpirySecs(MethodInvocation invocation)
    {
        Class<?> targetClass = invocation.getThis().getClass();
        Synchronized syncAnno = targetClass.getAnnotation(Synchronized.class);
        int expirySecs = syncAnno.lockExpirySecs();
        return expirySecs;
    }

}
