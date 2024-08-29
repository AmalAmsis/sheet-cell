package sheet;

import jaxb.schema.generated.*;
import sheet.coordinate.CoordinateImpl;
import sheet.effectivevalue.EffectiveValue;
import sheet.cell.Cell;
import sheet.cell.CellImpl;
import sheet.coordinate.Coordinate;
import java.io.Serializable;
import java.util.*;

/**
 * SheetImpl is an implementation of the Sheet interface, representing a spreadsheet.
 * It manages the cells within the sheet, including operations for setting, updating, and retrieving cell data.
 * The class also handles the conversion of the sheet to and from an STL representation for serialization.
 */
public class SheetImpl implements Sheet , Serializable {

    private int version;  // The version number of the sheet
    private final String title; // The title of the sheet
    private final Map<String, Cell> board = new HashMap<>();// A map storing the cells in the sheet
    private final int numOfRows; // The number of rows in the sheet
    private final int numOfCols; // The number of columns in the sheet
    private final int heightOfRows; // The height of rows in the sheet
    private final int widthOfCols; // The width of columns in the sheet


    /** Constructs a SheetImpl with specified dimensions and title.
     * @param title the title of the sheet.
     * @param numOfRows the number of rows in the sheet.
     * @param numOfCols the number of columns in the sheet.
     * @param heightOfRows the height of the rows in the sheet.
     * @param widthOfCols the width of the columns in the sheet. */
    public SheetImpl(String title, int numOfRows, int numOfCols, int heightOfRows, int widthOfCols) {
        this.version = 1;
        this.title = title;
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        this.heightOfRows = heightOfRows;
        this.widthOfCols = widthOfCols;
        //this.board = new HashMap<>();
    }

    /** Constructs a SheetImpl from an STL object, typically loaded from an XML file.
     * The version is set to 1 when loading the sheet.
     * @param stlSheet the STL sheet object representing the sheet's metadata.
     * @param sortedListOfStlCells a list of STL cells to be added to the sheet. */
    public SheetImpl(STLSheet stlSheet, List<STLCell> sortedListOfStlCells) {
        this.version =1; //when we load a sheet the version is 1.
        this.title = stlSheet.getName();
        this.numOfRows =stlSheet.getSTLLayout().getRows();
        this.numOfCols =stlSheet.getSTLLayout().getColumns();
        this.heightOfRows = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        this.widthOfCols = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();
        this.addSortedListOfStlCellsToSheet(sortedListOfStlCells); //load the cell on the list to our map
    }
    /** Adds a sorted list of STL cells to the sheet.
     * @param sortedListOfStlCells a list of STLCell objects to be added to the sheet. */
    private void addSortedListOfStlCellsToSheet(List<STLCell> sortedListOfStlCells) {
        for(STLCell stlCell : sortedListOfStlCells) {
            String originalValue = stlCell.getSTLOriginalValue();//?????????????????????????????????????????????????
            Coordinate myCoordinate = new CoordinateImpl(stlCell);
            this.addCell(myCoordinate, originalValue);
        }
        this.version = 1;
    }

    @Override
    public int getHeightOfRows() {
        return heightOfRows;
    }

    @Override
    public int getWidthOfCols() {
        return widthOfCols;
    }

    @Override
    public int getNumOfRows() {
        return numOfRows;
    }

    @Override
    public int getNumOfCols() {
        return numOfCols;
    }

    @Override
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
    public Cell getCell(Coordinate coordinate) {
        if (isCellInSheet(coordinate)) {
            return board.get(coordinate.toString());
        }
        else
        {
            return new CellImpl(coordinate, 0, this);
        }
    }

    @Override
    public int setCell(Coordinate coordinate, String originalValue) {
        updateVersion();// Every change in a cell updates the sheet version.
        int numOfUpdatededCells = 0;
        try {
            if (!isCellInSheet(coordinate)) {
                numOfUpdatededCells = addCell(coordinate, originalValue);
            } else {
                numOfUpdatededCells = updateCell(coordinate, originalValue);
            }
            return numOfUpdatededCells;

        }catch (Exception e){
            this.version --;
            throw e;
        }
    }

    /** Updates the sheet version. Each update increases the version number by one */
    private void updateVersion() {
        version++;
    }

    @Override
    public boolean isCellInSheet(Coordinate coordinate) {
        String key = coordinate.toString();
        return board.containsKey(key);
    }

    @Override
    public void addDependentCell(Coordinate mainCellCoordinate, Coordinate effectorCellCoordinate) {
        Cell mainCell = getCell(mainCellCoordinate);
        Cell effectorCell = getCell(effectorCellCoordinate);

        //mainCell.removeAllDependsOn();
        mainCell.addToDependsOn(effectorCell);
        //effectorCell.addInfluencingOn(mainCell);??????????????????????????????
    }

    @Override
    public EffectiveValue getCellEffectiveValue(Coordinate coordinate) {
        if (isCellInSheet(coordinate)) {
            return board.get(coordinate.toString()).getEffectiveValue();
        }
        return null;
    }

    /** Adds a new cell to the sheet.
     * @param coordinate the coordinate of the cell to add.
     * @param originalValue the original value to set in the cell.
     * @return the number of cells that been update. */
    public int addCell(Coordinate coordinate, String originalValue) {
        try {
            int numOfUpdatededCells = 0;
            Cell myCell = new CellImpl(coordinate,this.version,this);
            board.put(coordinate.toString(), myCell);
            numOfUpdatededCells = myCell.updateValue(originalValue);
            return numOfUpdatededCells;
        }catch (Exception e){
            board.remove(coordinate.toString());
            throw e;
        }
    }

    /** Updates the data of an existing cell in the sheet.
     * @param coordinate the coordinate of the cell to update.
     * @param originalValue the new value to set in the cell.
     * @return the number of cells that been update. */
    public int updateCell(Coordinate coordinate, String originalValue) {
        Cell myCell = board.get(coordinate.toString());
        myCell.removeAllDependsOn();
        int numOfUpdatededCells = 0;
        if(myCell != null) {
           numOfUpdatededCells = myCell.updateValue(originalValue);
        }
        return numOfUpdatededCells;
    }

    /** Removes a cell from the sheet.
     * @param coordinate the coordinate of the cell to remove. */
    public void removeCell(Coordinate coordinate) {

        board.remove(coordinate.toString());
    }

    /** Returns the letter of the last column in the sheet.
     * @return a char representing the last column letter. */
    public char getLastColLetter(){
        return (char) ('A' + numOfCols - 1);
    }

    @Override
    public Coordinate convertStringToCoordinate(String stringCoordinate) {
        // Check if the input is null or of incorrect length
        if (stringCoordinate == null || stringCoordinate.length() < 2 || stringCoordinate.length() > 3) {
            throw new IllegalArgumentException("Input must be between 2 to 3 characters long and non-null.");
        }


        // get the col letter and checking that a letter representing the column is in the col range of the sheet
        char col = stringCoordinate.toUpperCase().charAt(0);
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

    //not in was right now - maybe we will need it in te future
    /** Converts the current state of the sheet into an STLSheet object for serialization.
     * @return an STLSheet object representing the current state of the sheet. */
    public STLSheet convertFromSheetToStlSheet() {

        //create the ELEMENT STLCells
        STLCells stlCells = new STLCells();
        //create the list of STLCells
        List<STLCell> stlCellList = stlCells.getSTLCell();
        for (String key : board.keySet()) {

            //get the actual cell
            Cell cell = board.get(key);
            //create STLCell
            STLCell stlCell = cell.convertFromCellToSTLCell();
            //add the STLCell to the List
            stlCellList.add(stlCell);
        }

        //create the ELEMENT STLSize before STLLayot
        STLSize stlSize = new STLSize();
        stlSize.setRowsHeightUnits(heightOfRows);
        stlSize.setRowsHeightUnits(widthOfCols);

        //create the ELEMENT STLLayot
        STLLayout stlLayout = new STLLayout();
        stlLayout.setRows(numOfRows);
        stlLayout.setColumns(numOfCols);
        stlLayout.setSTLSize(stlSize);

        //and finally create the ELEMENT STLSheet
        STLSheet stlSheet = new STLSheet();
        stlSheet.setName(title);
        stlSheet.setSTLLayout(stlLayout);
        stlSheet.setSTLCells(stlCells);

        //STLCell stlCell = new STLCell();

        return stlSheet;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SheetImpl sheet = (SheetImpl) o;
        return version == sheet.version && numOfRows == sheet.numOfRows && numOfCols == sheet.numOfCols && heightOfRows == sheet.heightOfRows && widthOfCols == sheet.widthOfCols && Objects.equals(title, sheet.title) && Objects.equals(board, sheet.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, title, board, numOfRows, numOfCols, heightOfRows, widthOfCols);
    }
}
