package bs;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Demoes Spring bean lite mode.
 * <p>
 * See also <a href=
 * "http://stackoverflow.com/questions/26641349/why-does-this-java-based-spring-configuration-create-two-instances-of-a-singleto"
 * >Why does this Java based Spring configuration create two instances of a singleton bean?</a>
 */
public class App {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {

            System.out.println();
            System.out.println("The first singleton Bar in Foo is another instance than the second Bar:");
            System.out.println(ctx.getBean(Foo.class).toString());
            System.out.println(ctx.getBean(Bar.class).toString());
            System.out.println();
        }
    }
}
