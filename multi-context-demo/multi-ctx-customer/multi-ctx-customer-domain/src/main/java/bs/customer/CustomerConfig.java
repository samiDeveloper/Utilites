package bs.customer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan(basePackages = { "bs.customer" })
public class CustomerConfig {
    @Autowired
    Environment env;

    @Autowired
    private JpaDialect jpaDialect;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Bean(name = "entityManagerFactoryCustomer")
    public FactoryBean<EntityManagerFactory> entityManagerFactoryCustomer() {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setPersistenceXmlLocation("classpath:META-INF/persistence-customer.xml");
        emfBean.setPersistenceUnitName("multi-ctx-customer");
        emfBean.setDataSource(dataSource);
        emfBean.setJpaVendorAdapter(jpaVendorAdapter);
        emfBean.setJpaDialect(jpaDialect);
        return emfBean;
    }

    @Bean(name = "transactionManagerCustomer")
    public PlatformTransactionManager transactionManagerCustomer() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        try {
            transactionManager.setEntityManagerFactory(entityManagerFactoryCustomer().getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        transactionManager.setJpaDialect(jpaDialect);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
