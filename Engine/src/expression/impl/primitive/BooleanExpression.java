package expression.impl.primitive;

import expression.Expression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;


public class BooleanExpression implements Expression {

    private boolean value;

    public BooleanExpression(boolean value) {
        this.value = value;
    }

    @Override
    public EffectiveValue evaluate() {
        return new EffectiveValueImpl(CellType.BOOLEAN, value);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}
