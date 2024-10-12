package sheetmanager.expression.impl.math;

import sheetmanager.expression.Expression;
import sheetmanager.expression.impl.BinaryExpression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;

public class Percent extends BinaryExpression {
    public Percent(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {
        EffectiveValue result;
        if(!isValid(value1,value2)){
            result = new EffectiveValueImpl(CellType.NUMERIC, Double.NaN);
        }
        else{
            double part = value1.extractValueWithExpectation(Double.class);
            double whole = value2.extractValueWithExpectation(Double.class);
            double percent = part*whole/100;
            result = new EffectiveValueImpl(CellType.NUMERIC, percent);
        }
        return result;
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2) {
        boolean isValue1Valid = (value1.getCellType() == CellType.NUMERIC
                                && !Double.isNaN(value1.extractValueWithExpectation(Double.class))
                                && value1.extractValueWithExpectation(Double.class) >= 0);
        boolean isValue2Valid = (value2.getCellType() == CellType.NUMERIC
                                 && !Double.isNaN(value2.extractValueWithExpectation(Double.class)));
        return isValue1Valid && isValue2Valid;
    }
}
