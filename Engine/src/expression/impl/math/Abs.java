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
    protected boolean isValid(EffectiveValue value) {
        boolean result = (value.getCellType() == CellType.NUMERIC);
        return result;
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
