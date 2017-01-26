package bs.beansynchronizer;

import java.time.Clock;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import lombok.val;

/** Stores bean synchronization lock in a Map, only for testing purposes */
@Value
class MapBeanLocker implements BeanLocker
{
    @Getter(AccessLevel.NONE)
    ConcurrentMap<BeanName, Lock> beanLocks = new ConcurrentHashMap<>();

    @Getter(AccessLevel.NONE)
    Clock clock;

    /** True means that the client has clearance to access the bean identified by targetBean */
    @Override
    public boolean acquireLock(UUID clientId, BeanName targetBean, int expiryMins)
    {
        val now = clock.instant();
        Function<? super BeanName, ? extends Lock> f = (Void) -> Lock.forClient(clientId, now, expiryMins);
        val existingLock = beanLocks.get(targetBean);

        if (existingLock == null || !existingLock.isValidAt(now))
        {
            // absent or expired, take the lock
            beanLocks.put(targetBean, f.apply(targetBean));
            return true;
        } else if (existingLock.isOwnedBy(clientId))
        {
            // got the lock
            beanLocks.put(targetBean, existingLock.renew(now));
            return true;
        } else
        {
            // lock is owned by another node
            return false;
        }
    }

    @Override
    public void releaseAllLocks()
    {
        beanLocks.clear();
    }
}
