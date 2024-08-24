package expression.impl.primitive;

import expression.Expression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;


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
