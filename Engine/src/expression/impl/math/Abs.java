package expression.impl.math;

import expression.Expression;
import expression.impl.UnaryExpression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;


public class Abs extends UnaryExpression {

    public Abs(Expression expression) {
        super(expression);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value) {
        double result = Math.abs(value.extractValueWithExpectation(Double.class));
        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    protected void isValid(EffectiveValue value) {
       if (value.getCellType() != CellType.NUMERIC) {
           String message = String.format(
                   "Invalid operation: ABS requires argument to be numeric. " +
                           "Received: value=%s (type=%s)",
                   value.extractValueWithExpectation(Object.class).toString(),
                   value.getCellType().toString()
           );
           throw new IllegalArgumentException(message);
       }

    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
