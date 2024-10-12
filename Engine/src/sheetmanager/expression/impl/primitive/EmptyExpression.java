package sheetmanager.expression.impl.primitive;

import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;
import sheetmanager.expression.Expression;

public class EmptyExpression implements Expression {


    @Override
    public EffectiveValue evaluate() {
        return new EffectiveValueImpl(CellType.EMPTY, "");
    }

}
