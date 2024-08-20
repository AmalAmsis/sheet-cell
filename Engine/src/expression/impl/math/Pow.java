package expression.impl.math;

import expression.Expression;
import expression.impl.BinaryExpression;
import sheet.api.CellType;
import sheet.api.EffectiveValue;
import sheet.impl.EffectiveValueImpl;

public class Pow extends BinaryExpression {

    public Pow(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {
        // do some checking... error handling...
        double result = Math.pow(value1.extractValueWithExpectation(Double.class) , value2.extractValueWithExpectation(Double.class));
        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    // שניהם שווים 0
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2) {
        boolean result = (value1.getCellType() == CellType.NUMERIC && value2.getCellType() == CellType.NUMERIC );
        return result;
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
