package bs.app;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// TODO move bs.customer scan and transactionmanagement to domain project
@Configuration
@EnableTransactionManagement(proxyTargetClass=true)
@ComponentScan(basePackages = { "bs.app", "bs.order", "bs.customer" })
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Autowired
    Environment env;

    @Bean(name="entityManagerFactoryCustomer")
    public FactoryBean<EntityManagerFactory> entityManagerFactoryCustomer() {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setPersistenceXmlLocation("classpath:META-INF/persistence-customer.xml");
        emfBean.setPersistenceUnitName("multi-ctx-customer");
        emfBean.setDataSource(dataSource());
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());
        emfBean.setJpaDialect(jpaDialect());
        return emfBean;
    }

    @Bean(name="transactionManagerCustomer")
    public PlatformTransactionManager transactionManagerCustomer() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        try {
            transactionManager.setEntityManagerFactory(entityManagerFactoryCustomer().getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        transactionManager.setJpaDialect(jpaDialect());
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean(name="entityManagerFactoryOrder")
    public FactoryBean<EntityManagerFactory> entityManagerFactoryOrder() {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setPersistenceXmlLocation("classpath:META-INF/persistence-order.xml");
        emfBean.setPersistenceUnitName("multi-ctx-order");
        emfBean.setDataSource(dataSource());
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());
        emfBean.setJpaDialect(jpaDialect());
        return emfBean;
    }
    
    @Bean(name="transactionManagerOrder")
    public PlatformTransactionManager transactionManagerOrder() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        try {
            transactionManager.setEntityManagerFactory(entityManagerFactoryOrder().getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        transactionManager.setJpaDialect(jpaDialect());
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.valueOf(env.getProperty("jpa.vendor.database")));
        jpaVendorAdapter.setDatabasePlatform(env.getProperty("hibernate.dialect"));
        jpaVendorAdapter.setGenerateDdl(Boolean.valueOf(env.getProperty("jpa.vendor.generate.ddl")));
        jpaVendorAdapter.setShowSql(true);
        return jpaVendorAdapter;
    }

    @Bean
    public JpaDialect jpaDialect() {
        return new HibernateJpaDialect();
    }

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(env.getProperty("jdbc.driver"));
        ds.setUsername(env.getProperty("jdbc.username"));
        ds.setPassword(env.getProperty("jdbc.password"));
        ds.setUrl(env.getProperty("jdbc.url"));
        ds.setMaxActive(10);
        
        // assume h2 here and expose a port to connect remotely using url 'jdbc:hsqldb:tcp://localhost:9092/mem:multi-ctx-db;IFEXISTS=TRUE'
//        org.hsqldb.Server server = new org.hsqldb.Server();
//        server.setDaemon(true);
//        server.setPort(9092);
//        server.start();
        
        return ds;
    }
}
