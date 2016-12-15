package bs.interceptbeanname;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class InterceptBeanNameDemo
{
    public static void main(String[] args)
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(InterceptBeanNameDemo.class);
        
        // Talk to an interface.
        // Interface enables us to choose between JDK proxy and CGLIB proxy on the DefaultAdvisorAutoProxyCreator
        Foo foo1 = ctx.getBean("foo-bean-1", Foo.class);
        Foo foo2 = ctx.getBean("foo-bean-2", Foo.class);
        foo1.go();
        foo2.go();
    }
    
}
