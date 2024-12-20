package sheetmanager.expression.impl.math;

import sheetmanager.expression.Expression;
import sheetmanager.expression.impl.UnaryExpression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;


public class Abs extends UnaryExpression {

    public Abs(Expression expression) {
        super(expression);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value) {
        double result;
        if(!isValid(value)) {
            result = Double.NaN;
        }
        else {
            result = Math.abs(value.extractValueWithExpectation(Double.class));
        }
        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    protected boolean isValid(EffectiveValue value) {
        return (value.getCellType() == CellType.NUMERIC);
    }

//    @Override
//    protected void isValid(EffectiveValue value) {
//       if (value.getCellType() != CellType.NUMERIC) {
//           String message = String.format(
//                   "Invalid operation: ABS requires argument to be numeric. " +
//                           "Received: value=%s (type=%s)",
//                   value.getValue().toString(),
//                   value.getCellType().toString()
//           );
//           throw new IllegalArgumentException(message);
//       }
//
//    }
}
