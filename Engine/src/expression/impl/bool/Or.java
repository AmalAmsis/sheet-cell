package expression.impl.bool;

import expression.Expression;
import expression.impl.BinaryExpression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;

public class Or extends BinaryExpression {
    public Or(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {
        EffectiveValue result;
        if(!isValid(value1, value2)){
            result = new EffectiveValueImpl(CellType.BOOLEAN, "UNKNOWN");
        }
        else{
            boolean value = (value1.extractValueWithExpectation(Boolean.class) || value2.extractValueWithExpectation(Boolean.class));
            result = new EffectiveValueImpl(CellType.BOOLEAN, value);
        }
        return result;
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2) {
        boolean isValue1Valid = (value1.getCellType() == CellType.BOOLEAN && value1.getValue() != "UNKNOWN");
        boolean isValue2Valid = (value2.getCellType() == CellType.BOOLEAN && value2.getValue() != "UNKNOWN");
        return isValue1Valid && isValue2Valid;
    }
}
