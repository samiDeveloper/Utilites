package bs.beansynchronizer;

import lombok.Value;

@Value(staticConstructor = "of")
class BeanSyncTablePrefix
{
    private final String prefix;

    static BeanSyncTablePrefix empty()
    {
        return new BeanSyncTablePrefix("");
    }

    @Override
    public String toString()
    {
        return prefix;
    }
}
