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
class MapBeanLocker
{
    @Getter(AccessLevel.NONE)
    ConcurrentMap<BeanName, Lock> beanLocks = new ConcurrentHashMap<>();

    @Getter(AccessLevel.NONE)
    Clock clock;

    /** True means that the client has clearance to access the bean identified by targetBean */
    public boolean acquireLock(UUID clientId, BeanName targetBean)
    {
        val now = clock.instant();
        Function<? super BeanName, ? extends Lock> f = (Void) -> Lock.forClient(clientId, now);
        val existingLock = beanLocks.computeIfAbsent(targetBean, f);

        if (!existingLock.isValidAt(now))
        {
            // expired, take the lock
            beanLocks.putIfAbsent(targetBean, f.apply(targetBean));
            return true;
        } else if (existingLock.isOwnedBy(clientId))
        {
            // got the lock
            return true;
        } else
        {
            return false;
        }
    }

    public void releaseAllLocks()
    {
        beanLocks.clear();
    }
}
