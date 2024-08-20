package expression.impl.math;

import expression.Expression;
import expression.impl.BinaryExpression;
import sheet.api.CellType;
import sheet.api.EffectiveValue;
import sheet.impl.EffectiveValueImpl;

public class Minus extends BinaryExpression {

    public Minus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {
        if (!isValid(value1, value2)) {
            String message = String.format(
                    "Invalid operation: MINUS requires both arguments to be numeric. " +
                            "Received: value1=%s (type=%s), value2=%s (type=%s)",
                    value1.extractValueWithExpectation(Object.class).toString(),
                    value1.getCellType().toString(),
                    value2.extractValueWithExpectation(Object.class).toString(),
                    value2.getCellType().toString()
            );
            throw new IllegalArgumentException(message);
        }
        double result = value1.extractValueWithExpectation(Double.class) - value2.extractValueWithExpectation(Double.class);
        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2) {
        return value1.getCellType() == CellType.NUMERIC && value2.getCellType() == CellType.NUMERIC;
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
