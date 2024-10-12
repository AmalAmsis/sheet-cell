package sheetmanager.expression;

import sheetmanager.sheet.effectivevalue.EffectiveValue;

public interface Expression {
    EffectiveValue evaluate();
}
