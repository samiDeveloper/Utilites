Add a spring bean synchronization feature. Provides an @Synchronized annotation for bean-types of which only one instance should be active at a time across multiple JVMs.

Demo: run `SynchronizeDemo.java`

**Resources**

[Spring AOP](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#aop-introduction-defn)

**Cookbook**

To integrate bean synchronization in an application

1. Include the `SynchronizeConfig` java based spring configuration in the application context
2. Make sure that `Clock` and `DataSource` beans are available
3. Place the `@Synchronized` annotation on the beans that need synchronization

**Whishlist and ideas**

DataSourceBeanLocker (extract interface from MapBeanLocker). See also TODO in SynchronizeInterceptor


