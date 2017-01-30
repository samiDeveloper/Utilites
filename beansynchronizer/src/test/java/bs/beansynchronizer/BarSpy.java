package bs.beansynchronizer;

@Synchronized
public class BarSpy implements Bar
{
    private boolean goInvoked;

    @Override
    public void go()
    {
        goInvoked = true;
    }

    public boolean goInvoked()
    {
        return goInvoked;
    }

    public void reset()
    {
        goInvoked = false;
    }
}
