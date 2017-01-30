package bs.beansynchronizer;

import java.time.Clock;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

/** Stores bean synchronization lock in a Map, only for testing purposes */
@Value
class MapBeanLocker implements BeanLocker
{
    @Getter(AccessLevel.NONE)
    MapLockSource beanLocks = new MapLockSource();

    @Getter(AccessLevel.NONE)
    Clock clock;

    private static class MapLockSource implements LockSource
    {
        private ConcurrentMap<BeanName, Lock> beanLocks = new ConcurrentHashMap<>();

        @Override
        public void put(BeanName beanName, Lock lock)
        {
            beanLocks.put(beanName, lock);
        }

        @Override
        public Optional<Lock> get(BeanName beanName)
        {
            return Optional.ofNullable(beanLocks.get(beanName));
        }

        public void clear()
        {
            beanLocks.clear();
        }

        public void deleteLocksForClient(UUID clientId)
        {
            Set<BeanName> beansToDelete = beanLocks.entrySet()
                    .stream()
                    .filter((beanAndLock) -> beanAndLock.getValue()
                            .getClientId()
                            .equals(clientId))
                    .map(beanAndLock -> beanAndLock.getKey())
                    .collect(Collectors.toSet());
            for (BeanName bean : beansToDelete)
            {
                beanLocks.remove(bean);
            }
        }

        public int size()
        {
            return beanLocks.size();
        }

        public void deleteExpired(Clock clock)
        {
            Set<BeanName> toDelete = LockSupport.filterExpired(beanLocks, clock);
            for (BeanName bean : toDelete)
            {
                beanLocks.remove(bean);
            }
        }
    }

    public boolean acquireLock(UUID clientId, BeanName targetBean, int expirySecs)
    {
        return LockSupport.acquireLock(beanLocks, clientId, targetBean, expirySecs, clock);
    }

    @Override
    public void releaseLocksForClient(UUID clientId)
    {
        beanLocks.deleteLocksForClient(clientId);
    }

    @Override
    public void releaseAllLocks()
    {
        beanLocks.clear();
    }

    @Override
    public int size()
    {
        return beanLocks.size();
    }

    @Override
    public void cleanupExpired()
    {
        this.beanLocks.deleteExpired(clock);
    }
}
