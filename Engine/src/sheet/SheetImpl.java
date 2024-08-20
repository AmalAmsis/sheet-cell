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

    int version;
    String title;
    Map<String, Cell> board = new HashMap<>();
    final int numOfRows;
    final int numOfCols;
    final int heightOfRows;
    final int widthOfCols;

    //CTOR
    public SheetImpl(String title, int numOfRows, int numOfCols, int heightOfRows, int widthOfCols) {
        this.version = 0;
        this.title = title;
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        this.heightOfRows = heightOfRows;
        this.widthOfCols = widthOfCols;
    }

    public int getHeightOfRows() {
        return heightOfRows;
    }
    public int getWidthOfCols() {
        return widthOfCols;
    }
    public int getNumOfRows() {
        return numOfRows;
    }
    public int getNumOfCols() {
        return numOfCols;
    }

    public Map<String, Cell> getBoard() {
        return board;
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
    public Cell getCell(Coordinate coordinate) { // to do
      return board.get(coordinate.toString());
    }

    @Override
    public void setCell(Coordinate coordinate, String originalValue) {
        updateVersion(); //every change in cell mean that the version of the sheet change.

        if(originalValue.isBlank()) //if the original value contain only white spaces
       {
           removeCell(coordinate); //Whether the cell existed before or not, the result will be the same, there will no longer be this cell.
           return;
       }
        //*AMAL* to do
        EffectiveValue effectiveValue = calculateEffectiveValue(originalValue);

        if(!isCellInSheet(coordinate)) {
            addCell(coordinate, originalValue,effectiveValue);
        }
        else{
            updateCell(coordinate,originalValue,effectiveValue);
        }
    }

    //*AMAL* to do
    public EffectiveValue calculateEffectiveValue(String originalValue) {
        return null;
    }

    public void updateVersion(){
    version++;
    }

    //this func check if the sheet contain the coordinate
    public boolean isCellInSheet(Coordinate coordinate) {
        String key = coordinate.toString();
        return board.containsKey(key);
    }

    //add new cell to our board
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
