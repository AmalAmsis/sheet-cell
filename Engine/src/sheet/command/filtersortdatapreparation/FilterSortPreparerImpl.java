package sheet.command.filtersortdatapreparation;

import sheet.Sheet;
import sheet.cell.Cell;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;

import java.util.ArrayList;
import java.util.List;

public class FilterSortPreparerImpl implements FilterSortPreparer {

    Sheet sheet;

    public FilterSortPreparerImpl(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override public void sortValidator(Coordinate from, Coordinate to, List<Character> listOfColumnsPriorities) throws ValidationException {


        if (from.getRow() > to.getRow() || from.getCol() > to.getCol()) {
            throw new ValidationException.InvalidRangeOrderException(from, to);
        }
        if (!isCoordinateWithinSheet(from) || !isCoordinateWithinSheet(to)) {
            throw new ValidationException.CoordinateOutOfBoundsException(from);
        }
        for (Character column : listOfColumnsPriorities) {
            if (!isColumnNumeric(from, to, column)) {
                throw new ValidationException.NonNumericColumnException();
            }
        }
    }

    @Override public void filterValidator(Coordinate from, Coordinate to) throws ValidationException {


        if (from.getRow() > to.getRow() || from.getCol() > to.getCol()) {
            throw new ValidationException.InvalidRangeOrderException(from, to);
        }
        if (!isCoordinateWithinSheet(from) || !isCoordinateWithinSheet(to)) {
            throw new ValidationException.CoordinateOutOfBoundsException(from);
        }

    }

    @Override public List<List<Cell>> getRowInRange(Coordinate from, Coordinate to) {
        List<List<Cell>> listOfRows = new ArrayList<>();

        for (int row = from.getRow(); row <= to.getRow(); row++) {
            List<Cell> currentRow = new ArrayList<>();
            for (char col = from.getCol(); col <= to.getCol(); col++) {

                Coordinate currentCoordinate = new CoordinateImpl(col, row);

                Cell cell = this.sheet.getCell(currentCoordinate);

                System.out.println("Adding cell at " + currentCoordinate + ": " + cell.getEffectiveValue().getValue());

                currentRow.add(cell);
            }
            listOfRows.add(currentRow);
        }

        return listOfRows;
    }

    @Override public Sheet getSheet(){
        return sheet;
    };


    private boolean isCoordinateWithinSheet(Coordinate coordinate) {
        int maxRow = sheet.getNumOfRows();
        char maxCol = (char) ('A' + sheet.getNumOfCols() - 1);

        return !(coordinate.getRow() < 1 || coordinate.getRow() > maxRow || coordinate.getCol() < 'A' || coordinate.getCol() > maxCol);
    }

    private boolean isColumnNumeric(Coordinate from, Coordinate to, Character column) {
        List<List<Cell>> rowsInRange = getRowInRange(from, to);

        char col = from.getCol();
        int columnIndex = column - col;
        boolean allNumeric = true;

        for (List<Cell> row : rowsInRange) {
            Cell cellInColumn = row.get(columnIndex);

            EffectiveValue effectiveValue = cellInColumn.getEffectiveValue();

            if (effectiveValue.getCellType() != CellType.NUMERIC) {
                allNumeric = false;
                break;
            }
        }

        return allNumeric;
    }

    @Override public Coordinate convertStringToCoordinate(String stringCoordinate) {
        // Check if the input is null or of incorrect length
        if (stringCoordinate == null || stringCoordinate.length() < 2 || stringCoordinate.length() > 3) {
            throw new IllegalArgumentException("Input must be between 2 to 3 characters long and non-null.");
        }

        // get the col letter and checking that a letter representing the column is in the col range of the sheet
        char col = stringCoordinate.toUpperCase().charAt(0);


        //the follow must be a number
        for (int i = 1; i < stringCoordinate.length(); i++) {
            if (!Character.isDigit(stringCoordinate.charAt(i))) {
                throw new IllegalArgumentException("The input format is invalid. It should be a letter followed by digits.");
            }
        }

        // get row number
        int row;
        try {
            row = Integer.parseInt(stringCoordinate.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Row must be a valid number.");
        }

        // create the coordinate
        return new CoordinateImpl(col, row);
    }

}