package expression.impl.primitive;

import expression.Expression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;


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
