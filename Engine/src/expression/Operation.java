package expression;

import expression.exception.InvalidOperationNameException;
import expression.impl.math.*;
import expression.impl.primitive.NumericExpression;
import expression.impl.str.Concat;
import expression.impl.str.Sub;
import expression.impl.systemic.Ref;
import sheet.SheetDataRetriever;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;



public enum Operation {


    PLUS(2) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Plus(expressions[0], expressions[1]).evaluate();
        }
    },
    MINUS(2) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Minus(expressions[0], expressions[1]).evaluate();
        }
    },
    TIMES(2) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Times(expressions[0], expressions[1]).evaluate();
        }
    },
    DIVIDE(2) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Divide(expressions[0], expressions[1]).evaluate();
        }
    },
    MOD(2) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Mod(expressions[0], expressions[1]).evaluate();
        }
    },
    POW(2) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Pow(expressions[0], expressions[1]).evaluate();
        }
    },
    ABS(1) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Abs(expressions[0]).evaluate();
        }
    },
    CONCAT(2) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Concat(expressions[0], expressions[1]).evaluate();
        }
    },
    SUB(3) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Sub(expressions[0], expressions[1], expressions[2]).evaluate();
        }
    },
    REF(1) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Ref(expressions[0], sheet, targetCoordinate).evaluate();
        }
    };


    private final int numberOfArguments;

    Operation(int numberOfArguments) {
        this.numberOfArguments = numberOfArguments;
    }

    public int getNumberOfArguments() {
        return numberOfArguments;
    }

    public static Operation fromString(String operationName) {
        try {
            return Operation.valueOf(operationName);
        } catch (IllegalArgumentException e) {
            throw new InvalidOperationNameException(operationName);
        }
    }

    abstract EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions);
}