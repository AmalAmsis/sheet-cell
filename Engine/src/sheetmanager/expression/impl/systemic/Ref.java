package sheetmanager.expression.impl.systemic;

import sheetmanager.expression.Expression;
import sheetmanager.expression.impl.UnaryExpression;
import sheetmanager.sheet.SheetDataRetriever;
import sheetmanager.sheet.coordinate.Coordinate;
import sheetmanager.sheet.effectivevalue.EffectiveValue;



public class Ref extends UnaryExpression {

    private final SheetDataRetriever sheet;
    private Coordinate targetCoordinate;

    public Ref(Expression expression, SheetDataRetriever sheet, Coordinate targetCoordinate) {
        super(expression);
        this.sheet = sheet;
        this.targetCoordinate = targetCoordinate;
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value) {


        // Ensure that the value is valid before proceeding
//        if (!isValid(value))
//        {
//          // לשאול מה לעשות במצב כזה? + מה לעשות אם נתנו מחרוזת שלא יכולה להיות קורדינטה
//        }
//        else {
            Coordinate coordinate = sheet.convertStringToCoordinate(value.extractValueWithExpectation(String.class).toUpperCase());
            EffectiveValue result = sheet.getCellEffectiveValue(coordinate);
            // Check if the cell is Empty
            //if (result.getCellType() == CellType.EMPTY) {
                //throw new IllegalArgumentException(String.format("Invalid operation: REF cannot be executed with an empty cell. The cell '%s' is Empty", coordinate));
            //}
            sheet.addDependentCell(this.targetCoordinate, coordinate);
            return sheet.getCellEffectiveValue(coordinate);
//        }
    }

    @Override
    protected boolean isValid(EffectiveValue value) {
        // Ensure a sheet is set
        if (sheet == null) {
            return false;
        }
        // להוסיף  עוד בדיקות
        else
        {
            return true;
        }




    }

//    @Override
//    protected void isValid(EffectiveValue value) {
//        // Ensure a sheet is set
//        if (sheet == null) {
//            throw new IllegalStateException("No sheet has been set.");
//        }
//
//        // Ensure the value is a valid string representing a coordinate
//        String cellReference = value.extractValueWithExpectation(String.class);
//        if (cellReference == null || cellReference.trim().isEmpty()) {
//            throw new IllegalArgumentException("Invalid operation: REF requires a non-empty string as an argument.");
//        }
//
//        // Convert the string to a Coordinate object
//        Coordinate coordinate = sheet.convertStringToCoordinate(cellReference);
//        if (coordinate == null) {
//            throw new IllegalArgumentException(String.format("Invalid operation: REF cannot convert '%s' to a valid coordinate.", cellReference));
//        }
//
//        // Check if the cell is Empty
//        if (!sheet.isCellInSheet(coordinate)) {
//            throw new IllegalArgumentException(String.format("Invalid operation: REF cannot be executed with an empty cell. The cell '%s' is Empty", coordinate));
//        }
//    }
}
