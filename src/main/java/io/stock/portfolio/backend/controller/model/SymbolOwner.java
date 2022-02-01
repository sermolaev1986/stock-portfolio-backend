package io.stock.portfolio.backend.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public
class SymbolOwner {
    private String symbol;
    private String owner;
}
