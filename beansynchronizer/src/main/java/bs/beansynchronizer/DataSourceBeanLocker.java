package bs.beansynchronizer;

import java.util.UUID;

import javax.sql.DataSource;

public class DataSourceBeanLocker implements BeanLocker
{
    private final DataSource dataSource;

    public DataSourceBeanLocker(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    @Override
    public void releaseAllLocks()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean acquireLock(UUID clientId, BeanName targetBean, int expiryMins)
    {
        // TODO Auto-generated method stub
        return false;
    }

}
