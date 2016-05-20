package bs.app;

import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Autowired
    private Environment env;

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

        try {

            // assume h2 here and expose a port to connect remotely using url
            // 'jdbc:h2db:tcp://localhost:9092/mem:multi-ctx-db;IFEXISTS=TRUE'
            Server.createTcpServer("-tcpDaemon", "-tcpPort", "9092").start();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ds;
    }
}
