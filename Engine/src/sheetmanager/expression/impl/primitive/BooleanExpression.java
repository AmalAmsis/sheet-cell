package sheetmanager.expression.impl.primitive;

import sheetmanager.expression.Expression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;


public class BooleanExpression implements Expression {

    private boolean value;

    public BooleanExpression(boolean value) {
        this.value = value;
    }

    @Override
    public EffectiveValue evaluate() {
        return new EffectiveValueImpl(CellType.BOOLEAN, value);
    }

}
