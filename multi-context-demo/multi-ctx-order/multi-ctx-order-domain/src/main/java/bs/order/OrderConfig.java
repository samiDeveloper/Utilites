package bs.order;

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
@ComponentScan(basePackages = { "bs.order"})
public class OrderConfig {
    @Autowired
    Environment env;

    @Autowired
    private JpaDialect jpaDialect;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Bean(name = "entityManagerFactoryOrder")
    public FactoryBean<EntityManagerFactory> entityManagerFactoryOrder() {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setPersistenceXmlLocation("classpath:META-INF/persistence-order.xml");
        emfBean.setPersistenceUnitName("multi-ctx-order");
        emfBean.setDataSource(dataSource);
        emfBean.setJpaVendorAdapter(jpaVendorAdapter);
        emfBean.setJpaDialect(jpaDialect);
        return emfBean;
    }

    @Bean(name = "transactionManagerOrder")
    public PlatformTransactionManager transactionManagerOrder() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        try {
            transactionManager.setEntityManagerFactory(entityManagerFactoryOrder().getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        transactionManager.setJpaDialect(jpaDialect);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
