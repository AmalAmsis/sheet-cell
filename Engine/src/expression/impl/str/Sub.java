package expression.impl.str;

import expression.Expression;
import expression.impl.TernaryExpression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;


public class Sub extends TernaryExpression {

    public Sub(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2, EffectiveValue value3) {

        isValid(value1, value2, value3);

        String source = value1.extractValueWithExpectation(String.class);
        double startDouble = value2.extractValueWithExpectation(Double.class);
        double endDouble = value3.extractValueWithExpectation(Double.class);

        int start = (int) startDouble;
        int end = (int) endDouble;

        // Validate indices
        if (start < 0 || end < 0 || start > end || end >= source.length()) {
            return new EffectiveValueImpl(CellType.STRING, "!UNDEFINED!");//?????????
        }

        // Perform substring operation
        String result = source.substring(start, end + 1);
        return new EffectiveValueImpl(CellType.STRING, result);
    }

    @Override
    protected void isValid(EffectiveValue value1, EffectiveValue value2, EffectiveValue value3) {
        String source = value1.extractValueWithExpectation(String.class);
        double startDouble = value2.extractValueWithExpectation(Double.class);
        double endDouble = value3.extractValueWithExpectation(Double.class);

        // Ensure indices are integers
        if (startDouble % 1 != 0 || endDouble % 1 != 0) {
            throw new IllegalArgumentException("Start and end indices must be integers.");
        }

        // Validate source string is not null
        if (source == null) {
            throw new IllegalArgumentException("Source string cannot be null.");
        }
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.STRING;
    }
}
