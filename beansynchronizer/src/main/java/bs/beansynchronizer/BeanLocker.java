package bs.beansynchronizer;

import java.util.UUID;

interface BeanLocker
{
    /** True means that the client has clearance to access the bean identified by targetBean */
    boolean acquireLock(UUID clientId, BeanName targetBean, int expirySecs);

    void releaseLocksForClient(UUID clientId);

    void releaseAllLocks();
    
    int size();
    
    void cleanupExpired();
}