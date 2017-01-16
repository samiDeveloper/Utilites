package bs.beansynchronizer;

import java.util.UUID;

interface BeanLocker
{
    void releaseAllLocks();

    boolean acquireLock(UUID clientId, BeanName targetBean, int expiryMins);
}