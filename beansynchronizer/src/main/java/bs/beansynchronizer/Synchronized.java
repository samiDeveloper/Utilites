package bs.beansynchronizer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks spring beans of which only one instance is to be active at a time across multiple JVMs. Useful for clustered
 * environments where background tasks run, like batch jobs and scheduled processes. It differs from conventional
 * synchronization in that clients do not wait for a lock if they cannot obtain a lock, instead the bean invocation is
 * <strong>aborted</strong>. Invocations on @Synchronized beans throw a {@link SynchronizedInvocationAborted} if the target bean instance is unable
 * to acquire a lock.
 * 
 * <p>
 * It works using a lock in a central database. The {@link SynchronizeInterceptor} implements the synchronization.
 * 
 * <p>
 * Place this annotation on the class, not the interface. See
 * <a href="http://stackoverflow.com/a/3120323/1481125">http://stackoverflow.com/a/3120323/1481125</a> {@linkplain } or
 * <a href=
 * "http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#transaction-declarative-annotations">
 * Spring ref transaction-declarative-annotations</a> - 'Spring recommends that you only annotate concrete classes'
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Synchronized
{
    String dataSource() default "processMgrDataSource";
}