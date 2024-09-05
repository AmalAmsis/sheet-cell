package expression.impl.primitive;

import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;
import expression.Expression;

public class EmptyExpression implements Expression {


    @Override
    public EffectiveValue evaluate() {
        return new EffectiveValueImpl(CellType.EMPTY, "");
    }

}
