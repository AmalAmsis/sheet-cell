package expression.impl.math;

import expression.Expression;
import expression.impl.BinaryExpression;
import sheet.api.CellType;
import sheet.api.EffectiveValue;
import sheet.impl.EffectiveValueImpl;

public class Mod extends BinaryExpression {
    public Mod(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {

        // Check validity before performing the addition
        if (value1.getCellType() != CellType.NUMERIC && value2.getCellType() != CellType.NUMERIC) {
            String message = String.format(
                    "Invalid operation: MOD requires both arguments to be numeric. " +
                            "Received: value1=%s (type=%s), value2=%s (type=%s)",
                    value1.extractValueWithExpectation(Object.class).toString(),
                    value1.getCellType().toString(),
                    value2.extractValueWithExpectation(Object.class).toString(),
                    value2.getCellType().toString()
            );
            throw new IllegalArgumentException(message);
        }

        // Validate value2 is not zero
        double divisor = value2.extractValueWithExpectation(Double.class);
        if (divisor == 0) {
            throw new ArithmeticException("Invalid operation: Modulus by zero is not allowed.");
        }

        // If valid, perform the modulus operation
        double result = value1.extractValueWithExpectation(Double.class) % divisor;
        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2) {
        return value1.getCellType() == CellType.NUMERIC &&
                value2.getCellType() == CellType.NUMERIC &&
                value2.extractValueWithExpectation(Double.class) != 0;
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
