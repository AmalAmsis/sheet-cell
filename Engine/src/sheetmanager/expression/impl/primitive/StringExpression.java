package sheetmanager.expression.impl.primitive;

import sheetmanager.expression.Expression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;


public class StringExpression implements Expression {

    private String value;

    public StringExpression(String value) {
        this.value = value;
    }

    @Override
    public EffectiveValue evaluate() {
        return new EffectiveValueImpl(CellType.STRING, value);
    }

}
