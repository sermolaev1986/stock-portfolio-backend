package io.stock.portfolio.backend.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HibernateProperties {
    private String dialect;
    private String ddlAuto;
    private boolean showSql;
}
