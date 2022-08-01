package io.stock.portfolio.backend.controller.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ExpenseDTO {
    private String category;
    private BigDecimal value;
}
