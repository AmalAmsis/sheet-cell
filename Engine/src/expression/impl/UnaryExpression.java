package expression.impl;

import expression.Expression;
import sheet.api.EffectiveValue;

public abstract class UnaryExpression implements Expression {

    private Expression expression;

    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public EffectiveValue evaluate() {
        return doEvaluate(expression.evaluate());
    }

    protected abstract EffectiveValue doEvaluate(EffectiveValue value);
    abstract protected boolean isValid(EffectiveValue value);
}
