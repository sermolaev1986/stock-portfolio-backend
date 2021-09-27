package io.stock.portfolio.backend.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PositionDTO {
    @NotBlank
    private String symbol;
    @NotNull
    private Integer stockCount;
    @NotNull
    private Float buyPrice;
    @NotBlank
    private String owner;
    @NotNull
    private LocalDateTime buyDate;
    @NotBlank
    private String broker;
    private Float dividends;
}
