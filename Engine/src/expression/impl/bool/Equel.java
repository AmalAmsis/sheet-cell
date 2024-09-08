package expression.impl.bool;

import expression.Expression;
import expression.impl.BinaryExpression;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;

public class Equel extends BinaryExpression {

    public Equel(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {

        EffectiveValue result;
        if (!isValid(value1, value2)) {
            result = new EffectiveValueImpl(CellType.BOOLEAN, "UNKNOWN");
        }
        else {
            //equal יצליח להשוות כמו שצריך?
            if (value1.getCellType() == value2.getCellType() && value1.getValue().equals(value2.getValue())) {
                result = new EffectiveValueImpl(CellType.BOOLEAN, true);
            } else {
                result = new EffectiveValueImpl(CellType.BOOLEAN, false);
            }
        }
        return result;
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2) {
        boolean value1IsValid = ((value1.getCellType() == CellType.NUMERIC && value1.extractValueWithExpectation(Double.class) != Double.NaN)
                                  ||(value1.getCellType() == CellType.STRING && value1.extractValueWithExpectation(String.class) != "!UNDEFINED!")
                                  || (value1.getCellType() == CellType.BOOLEAN && value1.getValue() != "UNKNOWN")
                                  || (value1.getCellType() == CellType.EMPTY));
        boolean value2IsValid = ((value2.getCellType() == CellType.NUMERIC && value2.extractValueWithExpectation(Double.class) != Double.NaN)
                                ||(value2.getCellType() == CellType.STRING && value2.extractValueWithExpectation(String.class) != "!UNDEFINED!")
                                || (value2.getCellType() == CellType.BOOLEAN && value2.getValue() != "UNKNOWN")
                                || (value2.getCellType() == CellType.EMPTY));
        return value1IsValid && value2IsValid;
    }

}
