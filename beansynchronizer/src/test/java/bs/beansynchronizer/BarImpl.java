package bs.beansynchronizer;

@Synchronized
public class BarImpl implements Bar
{
    @Override
    public void go()
    {
        System.out.println("BarImpl.go");
    }
}
