package sheetmanager.expression.impl.math;

import sheetmanager.expression.Expression;
import sheetmanager.expression.impl.BinaryExpression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;


public class Plus extends BinaryExpression {

    public Plus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {

        double result;
        if(!isValid(value1, value2)){
            result = Double.NaN;
        }
        // If both values are valid, perform the addition
        else {
            result = value1.extractValueWithExpectation(Double.class) + value2.extractValueWithExpectation(Double.class);
        }
        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2) {
        return (value1.getCellType() == CellType.NUMERIC && value2.getCellType() == CellType.NUMERIC);
    }

//    @Override
//    protected void isValid(EffectiveValue value1, EffectiveValue value2) {
//        // Check validity before performing the addition
//        if (value1.getCellType() != CellType.NUMERIC || value2.getCellType() != CellType.NUMERIC) {
//            String message = String.format(
//                    "Invalid operation: PLUS requires both arguments to be numeric. " +
//                            "Received: value1=%s (type=%s), value2=%s (type=%s)",
//                    value1.getValue().toString(),
//                    value1.getCellType().toString(),
//                    value2.getValue().toString(),
//                    value2.getCellType().toString()
//            );
//            throw new IllegalArgumentException(message);
//        }
//    }
}
