package bs.beansynchronizer;

/**
 * Thrown when a lock on an @Synchronized bean cannot be acquired at the invocation time. It is typically safe to catch
 * and ignore this exception.
 */
public class SynchronizedInvocationAborted extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public SynchronizedInvocationAborted() {
        super();
    }

    public SynchronizedInvocationAborted(String message) {
        super(message);
    }

}
