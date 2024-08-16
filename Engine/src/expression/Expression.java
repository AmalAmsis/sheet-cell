package expression;

import sheet.api.EffectiveValue;

public interface Expression {
    EffectiveValue evaluate();
}
