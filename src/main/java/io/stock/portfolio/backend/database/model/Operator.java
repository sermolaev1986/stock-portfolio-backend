package io.stock.portfolio.backend.database.model;

import java.math.BigDecimal;

public enum Operator {
    PLUS("+") {
        @Override
        public BigDecimal calculateAmountOfShares(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg.add(secondArg);
        }

        @Override
        public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg.add(secondArg);
        }
    },
    MINUS("-") {
        @Override
        public BigDecimal calculateAmountOfShares(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg.subtract(secondArg);
        }

        @Override
        public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg
                    .subtract(secondArg)
                    .multiply(new BigDecimal("0.725"));
        }
    },
    MULTIPLY("*") {
        @Override
        public BigDecimal calculateAmountOfShares(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg.multiply(secondArg);
        }

        @Override
        public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg;
        }
    };


    Operator(String action) {
    }

    public BigDecimal calculateAmountOfShares(BigDecimal firstArg, BigDecimal secondArg) {
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
        return BigDecimal.ZERO;
    }
}
