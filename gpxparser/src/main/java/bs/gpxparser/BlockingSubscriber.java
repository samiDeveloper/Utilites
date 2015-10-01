package bs.gpxparser;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Buffers elements for the iterator to traverse them. Iterator blocks until an element becomes available. The
 * hasNext() will return false if nothing is available after the specified timeout period.
 */
public class BlockingSubscriber implements GpxParseEventSubscriber, Iterator<GpxParseEvent>, Iterable<GpxParseEvent> {

    // yes, this blocking is exactly what we need here to wait for elements to become available
    final BlockingDeque<GpxParseEvent> queue = new LinkedBlockingDeque<>();  
    
    private GpxParseEvent next;

    private final int timeoutSec;
    
    public BlockingSubscriber(int timeoutSec) {
        super();
        this.timeoutSec = timeoutSec;
    }

    @Override
    public void handleEvent(GpxParseEvent event) {
        queue.addLast(event);
    }

    /** Waits for timeoutSec seconds for an element to become available */
    @Override
    public boolean hasNext() {
        try {
            GpxParseEvent nextOpt = queue.pollFirst(timeoutSec, TimeUnit.SECONDS);
            if (nextOpt != null) {

                next = nextOpt;
                return true;
            } else {

                return false;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GpxParseEvent next() {
        if (next == null) {
            throw new NoSuchElementException();
        }
        
        GpxParseEvent result = next;
        next = null;
        return result;
    }

    @Override
    public Iterator<GpxParseEvent> iterator() {
        return this;
    }
}
