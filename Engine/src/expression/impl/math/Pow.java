package expression.impl.math;

import expression.Expression;
import expression.impl.BinaryExpression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;

public class Pow extends BinaryExpression {

    public Pow(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {

        isValid(value1, value2);

        // Perform the power operation
        double base = value1.extractValueWithExpectation(Double.class);
        double exponent = value2.extractValueWithExpectation(Double.class);
        double result = Math.pow(base, exponent);

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    protected void isValid(EffectiveValue value1, EffectiveValue value2) {
        // Check that both values are numeric
        if (value1.getCellType() != CellType.NUMERIC || value2.getCellType() != CellType.NUMERIC) {
            String message = String.format(
                    "Invalid operation: POW requires both arguments to be numeric. " +
                            "Received: value1=%s (type=%s), value2=%s (type=%s)",
                    value1.getValue().toString(),
                    value1.getCellType().toString(),
                    value2.getValue().toString(),
                    value2.getCellType().toString()
            );
            throw new IllegalArgumentException(message);
        }

        // Check for 0^0 case
        //double base = value1.extractValueWithExpectation(Double.class);
        //double exponent = value2.extractValueWithExpectation(Double.class);
        //if (base == 0 && exponent == 0) {
            //throw new ArithmeticException("Invalid operation: 0^0 is undefined.");
        //}
    }
}
