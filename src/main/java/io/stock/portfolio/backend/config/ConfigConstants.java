package io.stock.portfolio.backend.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigConstants {

    public static final String APP_DATASOURCE = "appDataSource";
    public static final String APP_HIKARI_CONF = "appHikariConfig";
    public static final String HIBERNATE_CONF = "hibernateConfig";

    public static final String LIQUIBASE_DATASOURCE = "liquibaseDataSource";
    public static final String LIQUIBASE_HIKARI_CONF = "liquibaseHikariConfig";
}
