Add a spring bean synchronization feature. Provides an @Synchronized annotation for bean-types of which only one instance should be active at a time across multiple JVMs.

Demo: run `SynchronizeInterceptorIT.java`

**Cookbook**

To integrate bean synchronization in an application

1. Include the `SynchronizeConfig` java based spring configuration in the application context
2. Make sure that the application context provides a `Clock` and `DataSource` bean
3. Create the beanlock database table like: `CREATE TABLE [prefix]BEAN_LOCK (BEANID VARCHAR, CLIENTID VARCHAR, EXPIRYSECS INT, ISSUED_TIMESTAMP TIMESTAMP)`
4. Place the `@Synchronized` annotation on the beans that need synchronization

Other considerations:

- Optionally override the `@Bean public BeanSyncTablePrefix beanSyncTablePrefix()` bean to specify a table prefix
- The DataSource bean must be named 'beanSyncDataSource' if other DataSource beans exist in the context
- The `SynchronizeConfig` enables @AspectJ spring support, it specifies `@EnableAspectJAutoProxy(proxyTargetClass = false)`

**Resources**

[Spring AOP](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#aop-introduction-defn)

