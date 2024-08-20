package expression.impl.systemic;

import expression.Expression;
import expression.impl.UnaryExpression;
import sheet.api.CellType;
import sheet.api.EffectiveValue;

public class Ref extends UnaryExpression {

    public Ref(Expression expression) {
        super(expression);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value) {
        return null;
    }

    @Override
    protected boolean isValid(EffectiveValue value) {
        return false;
    }

    @Override
    public CellType getFunctionResultType() {
    return null;
    }
}
