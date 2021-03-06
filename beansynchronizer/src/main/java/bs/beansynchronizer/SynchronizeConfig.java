package bs.beansynchronizer;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.interceptor.ExposeBeanNameAdvisors;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/** Depends on existence of a Clock bean and a DataSource bean, preferably named 'beanSyncDataSource' */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class SynchronizeConfig
{
    private static final Logger LOG = LoggerFactory.getLogger(SynchronizeConfig.class);

    @Autowired
    private Clock beanSyncClock;

    @Autowired
    private DataSource beanSyncDataSource;

    @Bean
    public BeanLocker beanLocker()
    {
        return new DataSourceBeanLocker(beanSyncDataSource, beanSyncTablePrefix(), beanSyncClock);
    }

    @Bean
    public BeanSyncTablePrefix beanSyncTablePrefix()
    {
        return BeanSyncTablePrefix.empty();
    }

    /**
     * Not a bean
     */
    private SynchronizeInterceptor synchronizeInterceptor()
    {
        LOG.info("Creating bean synchroization advise");
        return new SynchronizeInterceptor(beanLocker());
    }

    /**
     * Not a bean
     */
    private Advisor synchronizeAdvisor()
    {
        LOG.info("Add a pointcut to the advise to create the bean synchronization advisor");
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        // See '@within':
        // http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#aop-pointcuts-designators
        String pointcutDef = String.format("@within(%s)", Synchronized.class.getName());
        pointcut.setExpression(pointcutDef);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, synchronizeInterceptor());
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator createDefaultAdvisorAutoProxyCreator()
    {
        LOG.info("Creating bean synchronization proxy creator to configure JVM transcending bean synchronization");

        Advisor synchronizeAdvisor = synchronizeAdvisor();

        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator()
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource)
            {
                LOG.info("Extend advisors chain with beanname exposer and sync advisor for bean '{}' of type '{}'", beanName, beanClass);

                List<Object> advisorsList = new ArrayList<>();

                // Add the advisor exposing the bean name
                Advisor beanNameExposer = ExposeBeanNameAdvisors.createAdvisorWithoutIntroduction(beanName);
                advisorsList.add(beanNameExposer);

                // Add the sync advisor which picks up the bean name to synchronize on it
                advisorsList.add(synchronizeAdvisor);

                // Join the new advisors with the exising ones if any
                Object[] advisors = super.getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
                if (advisors != null)
                {
                    advisorsList.addAll(Arrays.asList(advisors));
                }
                return advisorsList.toArray();
            };
        };
        proxyCreator.setProxyTargetClass(true); // we need cglib to proxy classes not only interfaces
        return proxyCreator;
    }
}
