package expression.impl;

import expression.Expression;
import sheet.effectivevalue.EffectiveValue;


public abstract class TernaryExpression implements Expression {

    private Expression expression1;
    private Expression expression2;
    private Expression expression3;

    public TernaryExpression(Expression expression1, Expression expression2, Expression expression3) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public EffectiveValue evaluate() {
        return doEvaluate( expression1.evaluate(),expression2.evaluate(), expression3.evaluate());
    }

    protected abstract EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2, EffectiveValue value3);
    abstract protected boolean isValid(EffectiveValue value1, EffectiveValue value2, EffectiveValue value3);
}
