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
public class DividendResponse {
    private String symbol;
    private BigDecimal dollarBruttoAmount;
    private BigDecimal euroBruttoAmount;
    private BigDecimal euroNettoAmount;
    private LocalDateTime paymentDate;
    private LocalDateTime exDate;
    private BigDecimal shareAmount;
    private BigDecimal amountPerShare;
}
