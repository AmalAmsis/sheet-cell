package expression.impl.bool;

import expression.Expression;
import expression.impl.BinaryExpression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;

public class Less extends BinaryExpression {
    public Less(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {
        EffectiveValue result;
        if (!isValid(value1, value2)){
            result = new EffectiveValueImpl(CellType.BOOLEAN, "UNKNOWN");
        }
        else {
            boolean value = (value1.extractValueWithExpectation(Double.class) <= value2.extractValueWithExpectation(Double.class));
            result = new EffectiveValueImpl(CellType.BOOLEAN, value);
        }
        return result;
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2) {
        boolean value1IsValid = (value1.getCellType() == CellType.NUMERIC && !Double.isNaN(value1.extractValueWithExpectation(Double.class)));
        boolean value2IsValid = (value2.getCellType() == CellType.NUMERIC && !Double.isNaN(value2.extractValueWithExpectation(Double.class)));
        return value1IsValid && value2IsValid;
    }
}
