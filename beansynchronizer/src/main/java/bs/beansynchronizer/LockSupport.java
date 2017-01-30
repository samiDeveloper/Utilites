package bs.beansynchronizer;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.val;

final class LockSupport
{
    private LockSupport() {
        super();
    }

    static Set<BeanName> filterExpired(Map<BeanName, Lock> locks, Clock clock)
    {
        Instant now = clock.instant();
        Set<BeanName> expired = locks.entrySet()
                .stream()
                .filter(entry -> !entry.getValue()
                        .isValidAt(now))
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());
        return expired;
    }

    /** True means that the client has clearance to access the bean identified by targetBean */
    static boolean acquireLock(LockSource beanLocks, UUID clientId, BeanName targetBean, int expirySecs, Clock clock)
    {
        val now = clock.instant();
        Function<? super BeanName, ? extends Lock> lockForBean = (Void) -> Lock.forClient(clientId, now, expirySecs);
        val existingLockOpt = beanLocks.get(targetBean);

        if (!existingLockOpt.isPresent())
        {
            // absent, take the lock
            beanLocks.put(targetBean, lockForBean.apply(targetBean));
            return true;
        }

        val existingLock = existingLockOpt.get();
        if (!existingLock.isValidAt(now))
        {
            // expired, take the lock
            beanLocks.put(targetBean, lockForBean.apply(targetBean));
            return true;
        }

        if (existingLock.isOwnedBy(clientId))
        {
            // got the lock
            beanLocks.put(targetBean, existingLock.renew(now));
            return true;
        }

        // lock is owned by another node
        return false;
    }
}
