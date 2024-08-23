package expression.impl.systemic;

import expression.Expression;
import expression.impl.UnaryExpression;
import sheet.SheetDataRetriever;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;

import static sheet.coordinate.CoordinateImpl.convertStringToCoordinate;


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
        isValid(value);  // Ensure that the value is valid before proceeding
        Coordinate coordinate = convertStringToCoordinate(value.extractValueWithExpectation(String.class));
        sheet.addDependentCell(this.targetCoordinate, coordinate);
        return sheet.getCellEffectiveValue(coordinate);
    }

    @Override
    protected void isValid(EffectiveValue value) {
        // Ensure a sheet is set
        if (sheet == null) {
            throw new IllegalStateException("No sheet has been set.");
        }

        // Ensure the value is a valid string representing a coordinate
        String cellReference = value.extractValueWithExpectation(String.class);
        if (cellReference == null || cellReference.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid operation: REF requires a non-empty string as an argument.");
        }

        // Convert the string to a Coordinate object
        Coordinate coordinate = convertStringToCoordinate(cellReference);
        if (coordinate == null) {
            throw new IllegalArgumentException(String.format("Invalid operation: REF cannot convert '%s' to a valid coordinate.", cellReference));
        }

        // Check if the cell exists in the sheet
        if (!sheet.isCellInSheet(coordinate)) {
            throw new IllegalArgumentException(String.format("Invalid operation: The cell '%s' does not exist in the sheet.", cellReference));
        }
    }
}
