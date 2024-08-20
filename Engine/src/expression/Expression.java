package expression;

import sheet.api.CellType;
import sheet.api.EffectiveValue;

public interface Expression {
    EffectiveValue evaluate();
    CellType getFunctionResultType();
}
