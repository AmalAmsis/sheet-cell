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
                throw new ValidationException.NonNumericColumnException(column);
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


}