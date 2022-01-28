package io.stock.portfolio.backend.database.model;

import java.math.BigDecimal;

public enum Operator {
    PLUS("+") {
        @Override
        public BigDecimal calculateAmountOfShares(BigDecimal firstArg, Integer secondArg) {
            return firstArg.add(new BigDecimal(secondArg));
        }

        @Override
        public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg.add(secondArg);
        }
    },
    MINUS("-") {
        @Override
        public BigDecimal calculateAmountOfShares(BigDecimal firstArg, Integer secondArg) {
            return firstArg.subtract(new BigDecimal(secondArg));
        }

        @Override
        public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg.subtract(secondArg);
        }
    },
    MULTIPLY("*") {
        @Override
        public BigDecimal calculateAmountOfShares(BigDecimal firstArg, Integer secondArg) {
            return firstArg.multiply(new BigDecimal(secondArg));
        }

        @Override
        public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg;
        }
    };


    Operator(String action) {
    }

    public BigDecimal calculateAmountOfShares(BigDecimal firstArg, Integer secondArg) {
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
        return BigDecimal.ZERO;
    }
}
