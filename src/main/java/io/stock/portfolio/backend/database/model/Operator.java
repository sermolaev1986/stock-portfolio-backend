package io.stock.portfolio.backend.database.model;

import java.math.BigDecimal;

public enum Operator {
    PLUS("+") {
        @Override
        public Integer calculateAmountOfShares(Integer firstArg, Integer secondArg) {
            return firstArg + secondArg;
        }

        @Override
        public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg.add(secondArg);
        }
    },
    MINUS("-") {
        @Override
        public Integer calculateAmountOfShares(Integer firstArg, Integer secondArg) {
            return firstArg - secondArg;
        }

        @Override
        public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg.subtract(secondArg);
        }
    },
    MULTIPLY("*") {
        @Override
        public Integer calculateAmountOfShares(Integer firstArg, Integer secondArg) {
            return firstArg * secondArg;
        }

        @Override
        public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
            return firstArg;
        }
    };


    Operator(String action) {
    }

    public Integer calculateAmountOfShares(Integer firstArg, Integer secondArg) {
        return 0;
    }

    public BigDecimal calculateTotalPrice(BigDecimal firstArg, BigDecimal secondArg) {
        return BigDecimal.ZERO;
    }
}
