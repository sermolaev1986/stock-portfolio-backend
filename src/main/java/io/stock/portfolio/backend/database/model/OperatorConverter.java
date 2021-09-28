package io.stock.portfolio.backend.database.model;

import javax.persistence.AttributeConverter;

public class OperatorConverter implements AttributeConverter<Operator, String> {
    @Override
    public String convertToDatabaseColumn(Operator operator) {
        switch (operator) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MULTIPLY:
                return "*";
            default:
                throw new IllegalArgumentException(String.format("Operator %s is not supported.", operator));
        }
    }

    @Override
    public Operator convertToEntityAttribute(String value) {
        switch (value) {
            case "+":
                return Operator.PLUS;
            case "-":
                return Operator.MINUS;
            case "*":
                return Operator.MULTIPLY;
            default:
                throw new IllegalArgumentException(String.format("Operator %s is not supported.", value));
        }
    }
}
