package expression.impl;

import expression.Expression;
import sheet.effectivevalue.EffectiveValue;


public abstract class BinaryExpression implements Expression {

    private Expression expression1;
    private Expression expression2;

    public BinaryExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public EffectiveValue evaluate() {
        return doEvaluate(expression1.evaluate(), expression2.evaluate());
    }

    protected abstract EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) ;
    abstract protected void isValid(EffectiveValue value1, EffectiveValue value2) ;

}
