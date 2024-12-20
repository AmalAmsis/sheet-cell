package sheetmanager.expression.impl.str;

import sheetmanager.expression.Expression;
import sheetmanager.expression.impl.BinaryExpression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;


public class Concat extends BinaryExpression {

    public Concat(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2) {

        String result;
        if(!isValid(value1, value2)){
            result = "!UNDEFINED!";
        }
        else {
            result = (value1.extractValueWithExpectation(String.class) + value2.extractValueWithExpectation(String.class)).trim();
        }
        return new EffectiveValueImpl(CellType.STRING, result);
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2) {
        return  ((value1.getCellType() == CellType.STRING && value2.getCellType() == CellType.STRING)
                  &&(value1.getValue() != "!UNDEFINED!" && value2.getValue() != "!UNDEFINED!"));
    }

//    @Override
//    protected void isValid(EffectiveValue value1, EffectiveValue value2) {
//        if (value1.getCellType() != CellType.STRING || value2.getCellType() != CellType.STRING) {
//            String message = String.format(
//                    "Invalid operation: CONCAT requires both arguments to be of type STRING. " +
//                            "Received: value1=%s (type=%s), value2=%s (type=%s)",
//                    value1.getValue().toString(),
//                    value1.getCellType().toString(),
//                    value2.getValue().toString(),
//                    value2.getCellType().toString()
//            );
//            throw new IllegalArgumentException(message);
//        }
//
//        if (value1 == null) {
//            throw new IllegalArgumentException("The first argument is null.");
//        }
//        if (value2 == null) {
//            throw new IllegalArgumentException("The second argument is null.");
//        }
//
//    }
}
