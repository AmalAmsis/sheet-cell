package expression.impl.bool;

import expression.Expression;
import expression.impl.UnaryExpression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;

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
