package expression;

import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;

public interface Expression {
    EffectiveValue evaluate();
    CellType getFunctionResultType();
}
