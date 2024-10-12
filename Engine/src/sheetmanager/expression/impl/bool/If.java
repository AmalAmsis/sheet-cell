package sheetmanager.expression.impl.bool;

import sheetmanager.expression.Expression;
import sheetmanager.expression.impl.TernaryExpression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;

public class If extends TernaryExpression {
    public If(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2, EffectiveValue value3) {
        EffectiveValue result;
        if(!isValid(value1, value2, value3)){
            result = new EffectiveValueImpl(CellType.BOOLEAN, "UNKNOWN");
        }
        else {
            if(value1.extractValueWithExpectation(Boolean.class)){
                result = value2;
            }
            else {
                result = value3;
            }
        }
        return result;
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2, EffectiveValue value3) {
        boolean isValue1Condition = (value1.getCellType() == CellType.BOOLEAN && value1.getValue() != "UNKNOWN");
        return isValue1Condition && (value2.getCellType() == value3.getCellType());
    }
}
