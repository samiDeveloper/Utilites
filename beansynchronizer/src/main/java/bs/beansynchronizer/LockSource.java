package bs.beansynchronizer;

import java.util.Optional;

/** This abstraction makes it possible to extract the acquireLock logic to the LockSupport */
public interface LockSource
{
    /** Overwrites any existing lock for beanName */
    void put(BeanName beanName, Lock lock);

    Optional<Lock> get(BeanName beanName);
}
