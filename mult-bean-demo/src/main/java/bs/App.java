package bs;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
