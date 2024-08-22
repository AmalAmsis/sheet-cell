package sheet;

import dto.DTOCell;
import dto.DTOCoordinate;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.coordinate.CoordinateImpl;
import sheet.effectivevalue.EffectiveValue;
import sheet.cell.Cell;
import sheet.cell.CellImpl;
import sheet.coordinate.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {

    private int version;
    private final String title;
    private final Map<String, Cell> board;
    private final int numOfRows;
    private final int numOfCols;
    private final int heightOfRows;
    private final int widthOfCols;

    //CTOR
    public SheetImpl(String title, int numOfRows, int numOfCols, int heightOfRows, int widthOfCols) {
        this.version = 0;
        this.title = title;
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        this.heightOfRows = heightOfRows;
        this.widthOfCols = widthOfCols;
        this.board = new HashMap<>();
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Cell getCell(Coordinate coordinate) {
        return board.get(coordinate.toString());
    }

    @Override
    public void setCell(Coordinate coordinate, String originalValue) {
        updateVersion(); // Every change in a cell updates the sheet version.

        if (originalValue.isBlank()) { // If the original value is blank
            removeCell(coordinate); // Remove the cell if the value is blank.
            return;
        }

        EffectiveValue effectiveValue = calculateEffectiveValue(originalValue);

        if (!isCellInSheet(coordinate)) {
            addCell(coordinate, originalValue, effectiveValue);
        } else {
            updateCell(coordinate, originalValue, effectiveValue);
        }
    }

    // Update the sheet version
    private void updateVersion() {
        version++;
    }

    // Check if a cell exists in the sheet
    public boolean isCellInSheet(Coordinate coordinate) {
        String key = coordinate.toString();
        return board.containsKey(key);
    }

    @Override
    public EffectiveValue getCellEffectiveValue(Coordinate coordinate) {
        if (isCellInSheet(coordinate)) {
            return board.get(coordinate.toString()).getEffectiveValue();
        }
        return null;
    }

    // Add a new cell to the sheet
    public void addCell(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue) {
        Cell myCell = new CellImpl(coordinate, originalValue,effectiveValue);
        board.put(coordinate.toString(), myCell);

    }

    //update cell data.
    public void updateCell(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue) {
        Cell myCell = board.get(coordinate.toString());
        if(myCell != null) {
            myCell.setOriginalValue(originalValue);
            myCell.setLastModifiedVersion(version);
        }
    }

    // Remove a cell from the sheet
    public void removeCell(Coordinate coordinate) {
        board.remove(coordinate.toString());
    }

    public char getLastColLetter(){
        return (char) ('A' + numOfCols - 1);
    }


    public CoordinateImpl convertStringToCoordinate(String stringCoordinate) {
        // Check if the input is null or of incorrect length
        if (stringCoordinate == null || stringCoordinate.length() < 2 || stringCoordinate.length() > 3) {
            throw new IllegalArgumentException("Input must be between 2 to 3 characters long and non-null.");
        }


        // get the col letter and checking that a letter representing the column is in the col range of the sheet
        char col = stringCoordinate.charAt(0);
        if (col < 'A' || col > getLastColLetter()) {
            throw new IllegalArgumentException("Column must be a letter between A and " + getLastColLetter() + ".");
        }

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

        // check if is in the row range
        if (row < 1 || row > numOfRows) {
            throw new IllegalArgumentException("Row must be between 1 and " + numOfRows + ".");
        }

        // create the coordinate
        return new CoordinateImpl(col, row);
    }

}
