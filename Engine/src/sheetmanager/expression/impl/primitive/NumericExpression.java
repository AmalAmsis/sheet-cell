package sheetmanager.expression.impl.primitive;

import sheetmanager.expression.Expression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;


public class NumericExpression implements Expression {

    private double value;

    public NumericExpression(double value) {
        this.value = value;
    }

    @Override
    public EffectiveValue evaluate() {
        return new EffectiveValueImpl(CellType.NUMERIC, value);
    }

}
