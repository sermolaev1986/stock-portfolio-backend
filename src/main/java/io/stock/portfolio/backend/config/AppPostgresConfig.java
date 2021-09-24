package io.stock.portfolio.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static io.stock.portfolio.backend.config.ConfigConstants.*;
import static org.hibernate.cfg.AvailableSettings.*;

@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "io.stock.portfolio.backend.database")
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.app.postgres")
public class AppPostgresConfig {

    @Bean(APP_HIKARI_CONF)
    @ConfigurationProperties(prefix = "spring.datasource.app.postgres.hikari")
    public HikariConfig postgresHikariConfig() {
        return new HikariConfig();
    }

    @Bean(HIBERNATE_CONF)
    @ConfigurationProperties(prefix = "spring.datasource.app.postgres.jpa.hibernate")
    public HibernateProperties hibernateConfig() {
        return new HibernateProperties();
    }

    @Primary
    @Bean(APP_DATASOURCE)
    public DataSource dataSource(@Qualifier(APP_HIKARI_CONF) HikariConfig postgresHikariConfig) {
        return new HikariDataSource(postgresHikariConfig);
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier(HIBERNATE_CONF) HibernateProperties hibernateConf,
                                                                       @Qualifier(APP_DATASOURCE) DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean postgresEntityManager = builder
                .dataSource(dataSource)
                .packages("io.stock.portfolio.backend.database")
                .persistenceUnit("postgres")
                .build();

        postgresEntityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> properties = new HashMap<>();
        properties.put(HBM2DDL_AUTO, hibernateConf.getDdlAuto());
        properties.put(DIALECT, hibernateConf.getDialect());
        properties.put(SHOW_SQL, hibernateConf.isShowSql());
        postgresEntityManager.setJpaPropertyMap(properties);

        return postgresEntityManager;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
