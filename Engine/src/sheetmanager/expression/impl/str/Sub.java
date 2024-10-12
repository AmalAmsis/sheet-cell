package sheetmanager.expression.impl.str;

import sheetmanager.expression.Expression;
import sheetmanager.expression.impl.TernaryExpression;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;


public class Sub extends TernaryExpression {

    public Sub(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value1, EffectiveValue value2, EffectiveValue value3) {

        String result;
        if (!isValid(value1, value2, value3)){
            result = "!UNDEFINED!";
        }
        else {

            String source = value1.extractValueWithExpectation(String.class);
            double startDouble = value2.extractValueWithExpectation(Double.class);
            double endDouble = value3.extractValueWithExpectation(Double.class);

            int start = (int) startDouble;
            int end = (int) endDouble;


            // Perform substring operation
            result = source.substring(start, end + 1);
        }
        return new EffectiveValueImpl(CellType.STRING, result);
    }

    @Override
    protected boolean isValid(EffectiveValue value1, EffectiveValue value2, EffectiveValue value3) {

        String source = value1.extractValueWithExpectation(String.class);
        double startDouble = value2.extractValueWithExpectation(Double.class);
        double endDouble = value3.extractValueWithExpectation(Double.class);

        return ((value1.getCellType() == CellType.STRING && value2.getCellType() == CellType.NUMERIC && value3.getCellType() == CellType.NUMERIC )
                && (startDouble % 1 == 0 && endDouble % 1 == 0)
                &&(source == "!UNDEFINED!" && !Double.isNaN(startDouble) && Double.isNaN(endDouble))
                &&(startDouble > 0 && endDouble > 0 && startDouble < endDouble && endDouble <= source.length()));

    }

//    @Override
//    protected void isValid(EffectiveValue value1, EffectiveValue value2, EffectiveValue value3) {
//        if (value1.getCellType() != CellType.STRING || value2.getCellType() != CellType.NUMERIC || value3.getCellType() != CellType.NUMERIC ) {
//            String message = String.format(
//                    "Invalid operation: CONCAT requires first argument to be of type STRING, and the other two arguments to be of type NUMIRIC " +
//                            "Received: value1=%s (type=%s), value2=%s (type=%s), value3=%s (type=%s)",
//                    value1.getValue().toString(),
//                    value1.getCellType().toString(),
//                    value2.getValue().toString(),
//                    value2.getCellType().toString(),
//                    value3.getValue().toString(),
//                    value3.getCellType().toString()
//            );
//            throw new IllegalArgumentException(message);
//        }
//
//        String source = value1.extractValueWithExpectation(String.class);
//        double startDouble = value2.extractValueWithExpectation(Double.class);
//        double endDouble = value3.extractValueWithExpectation(Double.class);
//
//        // Ensure indices are integers
//        if (startDouble % 1 != 0 || endDouble % 1 != 0) {
//            throw new IllegalArgumentException("Start and end indices must be integers.");
//        }
//
//        // Validate source string is not null
//        if (source == null) {
//            throw new IllegalArgumentException("Source string cannot be null.");
//        }
//    }
}
