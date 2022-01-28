package io.stock.portfolio.backend.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PositionResponse {
    private String symbol;
    private String name;
    private BigDecimal stockCount;
    private String owner;
    private String broker;
    private LocalDateTime buyDate;
    private BigDecimal investments;
    private BigDecimal dividends;
}
