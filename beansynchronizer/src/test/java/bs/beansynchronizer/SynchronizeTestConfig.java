package bs.beansynchronizer;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.SimpleThreadScope;

@Configuration
public class SynchronizeTestConfig
{
    public static final String JDBC_DRIVER_NAME = "org.h2.Driver";
    public static final String USER_NAME = "sa";
    public static final String PASSWORD = "";

    public static final String THREAD_SCOPE = "thread";

    /** Register custom thread scope which is not available by default in spring contexts */
    @Bean
    public static CustomScopeConfigurer customScopeConfigurer()
    {
        CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put(THREAD_SCOPE, new SimpleThreadScope());
        customScopeConfigurer.setScopes(scopes);
        return customScopeConfigurer;
    }

//    /**
//     * Override and put in thread scope because only one test at a time must be able to access this mutable bean in a
//     * parallel testing scenario
//     */
//    @Scope(THREAD_SCOPE)
//    @Bean
//    public BeanLocker beanLocker()
//    {
//        return new MapBeanLocker(clock());
//    }

    @Bean(name = "processMgrDataSource", destroyMethod = "close")
    public DataSource dataSource()
    {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(JDBC_DRIVER_NAME);
        ds.setUsername(USER_NAME);
        ds.setPassword(PASSWORD);
        ds.setUrl("jdbc:h2:mem:processMgr");
        return ds;
    }

    @Bean
    public Foo foo()
    {
        return new FooImpl();
    }

    @Scope(SynchronizeTestConfig.THREAD_SCOPE)
    @Bean(name = "bar-customname")
    public Bar bar()
    {
        return new BarSpy();
    }

    /**
     * Thread scope because only one test at a time must be able to access this mutable bean in a parallel testing
     * scenario
     */
    @Bean
    @Scope(THREAD_SCOPE)
    public MutableClockStub clock()
    {
        return new MutableClockStub(Instant.parse("2016-12-11T14:00:00Z"));
    }
}
