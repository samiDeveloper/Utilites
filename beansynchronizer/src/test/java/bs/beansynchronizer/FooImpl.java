package bs.beansynchronizer;

import org.springframework.beans.factory.annotation.Autowired;

@Synchronized(lockExpirySecs = Foo.EXPIRY_SECS)
public class FooImpl implements Foo
{
    @Autowired
    private Bar bar;

    @Override
    public void start()
    {
        bar.go();
    }
}
