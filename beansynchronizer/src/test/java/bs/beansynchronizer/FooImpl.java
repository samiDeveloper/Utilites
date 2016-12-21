package bs.beansynchronizer;

import org.springframework.beans.factory.annotation.Autowired;

import bs.beansynchronizer.Synchronized;

@Synchronized(lockExpiryMins = Foo.EXPIRY_MINS)
public class FooImpl implements Foo
{
    @Autowired
    private Bar bar;

    @Override
    public void start()
    {
        System.out.println("FooImpl.start");
        bar.go();
    }
}
