package sheetmanager.expression.impl.bool;

import sheetmanager.expression.Expression;
import sheetmanager.expression.impl.UnaryExpression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;

public class Not extends UnaryExpression {

    public Not(Expression expression) {
        super(expression);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value) {
        EffectiveValue result;
        if(!isValid(value)){
            result = new EffectiveValueImpl(CellType.BOOLEAN, "UNKNOWN");
        }
        else {
            result = new EffectiveValueImpl(CellType.BOOLEAN, !(value.extractValueWithExpectation(Boolean.class)));
        }
        return result;
    }

    @Override
    protected boolean isValid(EffectiveValue value) {
        return (value.getCellType() == CellType.BOOLEAN && value.getValue() != "UNKNOWN");
    }
}
