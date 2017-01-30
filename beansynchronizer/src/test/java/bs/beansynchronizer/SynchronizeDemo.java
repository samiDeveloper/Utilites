package bs.beansynchronizer;

import java.time.Instant;
import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SynchronizeDemo
{
    private static final String BAR_CUSTOMNAME = "bar-customname";

    public static void main(String[] args)
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SynchronizeConfig.class, SynchronizeDemo.class);
        Foo foo = ctx.getBean(Foo.class);
        BeanLocker beanLocker = ctx.getBean(MapBeanLocker.class);

        // simulate another node to acquire the lock on the Bar bean
        beanLocker.acquireLock(UUID.randomUUID(), BeanName.of(BAR_CUSTOMNAME), Synchronized.DEFAULT_EXPIRY_SECONDS);

        // the foo invocation will proceed...
        try
        {
            foo.start();
        } catch (SynchronizedInvocationAborted sia)
        {
            // but foo's invocation to bar will abort
            System.out.println("Bean invocation aborted: " + sia);
        }
    }

    @Bean
    public BeanLocker beanLocker()
    {
        return new MapBeanLocker(clock());
    }

    @Bean
    public Foo foo()
    {
        return new FooImpl();
    }

    @Bean(name = BAR_CUSTOMNAME)
    public Bar bar()
    {
        return new BarSpy();
    }

    @Bean
    public MutableClockStub clock()
    {
        return new MutableClockStub(Instant.parse("2016-12-11T14:00:00Z"));
    }
}
