package bs.interceptbeanname;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.interceptor.ExposeBeanNameAdvisors;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true) // still works with global autoproxy enabled
public class InterceptBeanNameConfig
{
    // Using interfaces enables us to choose between JDK proxy and CGLIB proxy, see proxyCreator.setProxyTargetClass below
    // When exposing a class as the bean spring must use CGLIB
    
    @Bean(name = "foo-bean-1")
    public Foo foo1()
    {
        return new FooImpl();
    }

    @Bean(name = "foo-bean-2")
    public Foo foo2()
    {
        return new FooImpl();
    }

    // The Advice
    public MethodInterceptor myInterceptor()
    {
        return new MethodInterceptor()
        {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable
            {
                try
                {
                    String beanName = ExposeBeanNameAdvisors.getBeanName(invocation);
                    System.out.println(String.format("Bean name: '%s'", beanName));
                } catch (IllegalStateException e)
                {
                    System.out.println(e);
                }

                return invocation.proceed();
            }
        };
    }

    // Do not expose as bean to prevent @EnableAspectJAutoProxy see this advisor.
    // If @EnableAspectJAutoProxy picks the Advisor up then the Advice (MethodInterceptor) will not find the bean name
    // because ExposeBeanNameAdvisor is not in the interceptor chain
    // @Bean
    public Advisor myAdvisor()
    {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        pointcut.setExpression("execution(* *.go(..))");

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, myInterceptor());
        advisor.setOrder(2);
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator createDefaultAdvisorAutoProxyCreator()
    {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator()
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName,
                    TargetSource targetSource)
            {
                Object[] advisors = super.getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);

                List<Object> advisorsList = new ArrayList<>();

                // Here we add the ExposeBeanNameAdvisor to the interceptor chain.
                // It puts the bean name on the MethodInvocation object during a bean invocation.
                Advisor beanNameExposer = ExposeBeanNameAdvisors.createAdvisorWithoutIntroduction(beanName);
                advisorsList.add(beanNameExposer);

                // Add an advisor picking up the bean name
                advisorsList.add(myAdvisor());

                if (advisors != null)
                    advisorsList.addAll(Arrays.asList(advisors));
                return advisorsList.toArray();
            };
        };
        proxyCreator.setProxyTargetClass(true); // false = JDK proxy, true = CGLIB proxy
        return proxyCreator;
    }
}
