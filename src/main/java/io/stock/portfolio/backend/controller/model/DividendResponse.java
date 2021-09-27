package io.stock.portfolio.backend.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DividendResponse {
    private String symbol;
    private Float amountPerShare;
    private Float dollarBruttoAmount;
    private Float dollarNettoAmount;
    private Float euroBruttoAmount;
    private Float euroNettoAmount;
    private LocalDateTime paymentDate;
    private LocalDateTime exDate;
}
