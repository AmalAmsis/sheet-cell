package sheetmanager.expression;

import sheetmanager.expression.exception.InvalidOperationNameException;
import sheetmanager.expression.impl.bool.*;
import sheetmanager.expression.impl.math.*;
import sheetmanager.expression.impl.str.Concat;
import sheetmanager.expression.impl.str.Sub;
import sheetmanager.expression.impl.systemic.Ref;
import sheetmanager.sheet.SheetDataRetriever;
import sheetmanager.sheet.coordinate.Coordinate;
import sheetmanager.sheet.effectivevalue.EffectiveValue;


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
    },
    EQUAL(2) {
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Equel(expressions[0],expressions[1]).evaluate();
        }
    },
    NOT(1){
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Not(expressions[0]).evaluate();
        }
    },
    BIGGER(2){
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Bigger(expressions[0],expressions[1]).evaluate();
        }
    },
    LESS(2){
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Less(expressions[0],expressions[1]).evaluate();
        }
    },
    OR(2){
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Or(expressions[0],expressions[1]).evaluate();
        }
    },
    AND(2){
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new And(expressions[0],expressions[1]).evaluate();
        }
    },
    IF(3){
       @Override
       EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
           return new If(expressions[0], expressions[1], expressions[2]).evaluate();
       }
    },
    PERCENT(2){
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Percent(expressions[0], expressions[1]).evaluate();
        }
    },
    SUM(1){
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Sum(expressions[0], sheet, targetCoordinate).evaluate();
        }
    },
    AVERAGE(1){
        @Override
        EffectiveValue eval(SheetDataRetriever sheet, Coordinate targetCoordinate, Expression... expressions) {
            return new Average(expressions[0], sheet, targetCoordinate).evaluate();
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