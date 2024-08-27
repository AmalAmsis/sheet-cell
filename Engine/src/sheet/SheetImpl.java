package sheet;

import dto.DTOCell;
import dto.DTOCoordinate;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import jaxb.schema.generated.*;
import sheet.coordinate.CoordinateImpl;
import sheet.effectivevalue.EffectiveValue;
import sheet.cell.Cell;
import sheet.cell.CellImpl;
import sheet.coordinate.Coordinate;

import java.io.Serializable;
import java.util.*;

public class SheetImpl implements Sheet , Serializable {

    private int version;
    private final String title;
    private final Map<String, Cell> board = new HashMap<>();;
    private final int numOfRows;
    private final int numOfCols;
    private final int heightOfRows;
    private final int widthOfCols;


    //CTOR
    public SheetImpl(String title, int numOfRows, int numOfCols, int heightOfRows, int widthOfCols) {
        this.version = 1;
        this.title = title;
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        this.heightOfRows = heightOfRows;
        this.widthOfCols = widthOfCols;
        //this.board = new HashMap<>();
    }

    // 22/8/24 - this ctor from STL object that we got from xml file,
    // we assume that we will get it to the ctor after validation test!
    public SheetImpl(STLSheet stlSheet, List<STLCell> sortedListOfStlCells) {
        //when we load a sheet the version is 1.
        this.version =1;
        this.title = stlSheet.getName();
        this.numOfRows =stlSheet.getSTLLayout().getRows();
        this.numOfCols =stlSheet.getSTLLayout().getColumns();
        this.heightOfRows = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        this.widthOfCols = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();

        //load the cell on the list to our map
        this.addSortedListOfStlCellsToSheet(sortedListOfStlCells);

            /* לשאול את ירדן מה היא חושבת
            CellImpl cell = new CellImpl(stlCell);
            String key = cell.getId();
            board.put(key, cell);
             */

    }

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
        return board.get(coordinate.toString());
    }

    @Override
    public void setCell(Coordinate coordinate, String originalValue) {
        updateVersion(); // Every change in a cell updates the sheet version.
        try {
            /*
            if (originalValue.isBlank()) { // If the original value is blank
                removeCell(coordinate); // Remove the cell if the value is blank.
                return;
            }
             */
            if (!isCellInSheet(coordinate)) {
                addCell(coordinate, originalValue);
            } else {
                updateCell(coordinate, originalValue);
            }
        }catch (Exception e){
            this.version --;
            throw e;
        }
    }

    // Update the sheet version
    private void updateVersion() {
        version++;
    }

    // Check if a cell exists in the sheet
    @Override
    public boolean isCellInSheet(Coordinate coordinate) {
        String key = coordinate.toString();
        return board.containsKey(key);
    }

    @Override
    public void addDependentCell(Coordinate mainCellCoordinate, Coordinate effectorCellCoordinate) {
        Cell mainCell = getCell(mainCellCoordinate);
        Cell effectorCell = getCell(effectorCellCoordinate);

        mainCell.removeAllDependsOn();
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

    // Add a new cell to the sheet
    public void addCell(Coordinate coordinate, String originalValue) {
        try {
            Cell myCell = new CellImpl(coordinate,this.version,this);
            board.put(coordinate.toString(), myCell);
            myCell.updateValue(originalValue);
        }catch (Exception e){
            board.remove(coordinate.toString());
            throw e;
        }
    }

    //update cell data.
    public void updateCell(Coordinate coordinate, String originalValue) {
        Cell myCell = board.get(coordinate.toString());
        if(myCell != null) {
           myCell.updateValue(originalValue);
        }
    }

    // לא משתמשים
    // Remove a cell from the sheet
    public void removeCell(Coordinate coordinate) {

        board.remove(coordinate.toString());
    }

    public char getLastColLetter(){
        return (char) ('A' + numOfCols - 1);
    }


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

    //22/8/24 by yarden --> for creating a xml file from objects
    //very ugly function - TO DO it nice
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
