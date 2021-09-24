package io.stock.portfolio.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static io.stock.portfolio.backend.config.ConfigConstants.LIQUIBASE_DATASOURCE;
import static io.stock.portfolio.backend.config.ConfigConstants.LIQUIBASE_HIKARI_CONF;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.liquibase.postgres")
public class LiquibasePostgresConfig {

    @Bean(LIQUIBASE_HIKARI_CONF)
    @ConfigurationProperties(prefix = "spring.datasource.liquibase.postgres.hikari")
    public HikariConfig liquibaseHikariConfig() {
        return new HikariConfig();
    }

    @LiquibaseDataSource
    @Bean(LIQUIBASE_DATASOURCE)
    public DataSource dataSource(@Qualifier(LIQUIBASE_HIKARI_CONF) HikariConfig liquibaseHikariConfig) {
        return new HikariDataSource(liquibaseHikariConfig);
    }
}
