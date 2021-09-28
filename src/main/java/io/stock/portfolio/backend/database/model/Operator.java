package io.stock.portfolio.backend.database.model;

public enum Operator {
    PLUS("+"){
        @Override
        public Integer calculate(Integer firstArg, Integer secondArg){
            return firstArg + secondArg;
        }
    },
    MINUS("-"){
        @Override
        public Integer calculate(Integer firstArg, Integer secondArg){
            return firstArg - secondArg;
        }
    },
    MULTIPLY("*"){
        @Override
        public Integer calculate(Integer firstArg, Integer secondArg){
            return firstArg * secondArg;
        }
    };


    Operator(String action) {
    }

    public Integer calculate(Integer firstArg, Integer secondArg){
        return 0;
    }
}
