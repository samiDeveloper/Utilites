package bs;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Omitting @Configuration or using @Compenent here has an interesting effect where spring could create
 * multiple instances of the singleton Bar bean.
 * <p>
 * By the way, overriding beans is usually done by feeding the classes in a specific order to the
 * {@link AnnotationConfigApplicationContext}
 */
public class AppConfig extends BaseAppConfig {
    @Bean
    @Override
    public Bar bar() {
        return new Bar();
    }
}
