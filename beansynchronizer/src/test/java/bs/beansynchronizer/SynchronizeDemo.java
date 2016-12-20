package bs.beansynchronizer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class SynchronizeDemo
{
    public static void main(String[] args)
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SynchronizeDemo.class);
        Foo foo = ctx.getBean(Foo.class);
        foo.start();
    }
    
}
