package io.stock.portfolio.backend.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.stock.portfolio.backend.database.model.Operator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TransactionDTO {
    @NotNull
    private LocalDateTime date;
    @NotBlank
    private String symbol;
    @NotBlank
    private String owner;
    @NotNull
    private Integer argument;
    @NotNull
    private Operator operator;

    private Float price;
    private String broker;
}
