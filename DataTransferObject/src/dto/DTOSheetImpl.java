package dto;

import sheet.Sheet;
import sheet.SheetImpl;
import sheet.cell.Cell;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;
import sheet.range.Range;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTOSheetImpl is an implementation of the DTOSheet interface.
 * This class is used to create a data transfer object (DTO) for a spreadsheet,
 * encapsulating the sheet's state and structure, and can be serialized.
 */
public class DTOSheetImpl implements DTOSheet, Serializable {

    private int version;
    private String title;
    private Map<String, DTOCell> board = new HashMap<>();
    private int numOfRows;
    private int numOfCols;
    private int heightOfRows;
    private int widthOfCols;
    private char firstColumn = '0';
    private int firstRow = 1;

    //**********************************************YARDEN**********************************************//

    private final Map<String, DTORange> ranges;



    /**
     * Constructs a DTOSheetImpl from a Sheet object.
     * Initializes the DTOSheetImpl's properties based on the given Sheet.
     * @param sheet the Sheet object to convert into a DTOSheet.*/
    public DTOSheetImpl(Sheet sheet) {
        this.version = sheet.getVersion();
        this.title = sheet.getTitle();
        this.numOfRows = sheet.getNumOfRows();
        this.numOfCols = sheet.getNumOfCols();
        this.heightOfRows = sheet.getHeightOfRows();
        this.widthOfCols = sheet.getWidthOfCols();
        this.ranges = new HashMap<>();

        //create DTOCell from original board of cells.
        for( Cell cell : sheet.getBoard().values()) {
            DTOCell dtoCell = new DTOCellImpl(cell);
            addDTOCell(dtoCell);
        }

        //**********************************************YARDEN**********************************************//
        for(Range range : sheet.getRangeManager().getRanges().values()){
            this.ranges.put(range.getName(), new DTORangeImpl(range)) ;
        }
        //**********************************************YARDEN**********************************************//

    }



    @Override
    public Map<String,DTORange> getRanges(){
        return this.ranges;
    };

    public DTOSheetImpl(Sheet sheet, List<List<Cell>> sortedRows, Coordinate from, Coordinate to) {
        this(sheet);

        int rowIdx = from.getRow();
        char colIdx = from.getCol();
        Coordinate coordinate = new CoordinateImpl(colIdx, rowIdx);


        for (List<Cell> row : sortedRows) {
            for (Cell cell : row) {
                coordinate.setCol(colIdx);
                coordinate.setRow(rowIdx);
                DTOCell dtoCell = new DTOCellImpl(cell,coordinate);
                addDTOCell(dtoCell);
                colIdx++;
            }
            colIdx = from.getCol();
            rowIdx++;
        }
    }


    public DTOSheetImpl(List<List<Cell>> filteredRows , Coordinate from,Sheet sheet,Coordinate to) {
        this(sheet);

        int rowIdx = from.getRow();
        char colIdx = from.getCol();
        Coordinate coordinate = new CoordinateImpl(colIdx, rowIdx);

        int lastRowInRange = to.getRow();
        char firstColInRange = from.getCol();
        char lastColInRange = to.getCol();

        for (List<Cell> row : filteredRows) {
            for (Cell cell : row) {
                coordinate.setCol(colIdx);
                coordinate.setRow(rowIdx);
                DTOCell dtoCell = new DTOCellImpl(cell,coordinate);
                addDTOCell(dtoCell);
                colIdx++;
            }
            colIdx = from.getCol();
            rowIdx++;
        }

        while (rowIdx <= lastRowInRange) {
            for(int i=rowIdx; i<=lastRowInRange; i++) {
                char j;
                for (j = firstColInRange; j <= lastColInRange; j++) {
                    removeDTOCell(j, rowIdx);
                }
            }
            rowIdx++;
        }



    }
    //**********************************************YARDEN**********************************************//

    @Override public char getFirstColumnLetter(){
        return firstColumn;
    };

    @Override public int getFirstRow(){
        return firstRow;
    };


    @Override
    public int getSheetVersion() {
        return version;
    }

    @Override
    public String getSheetTitle() {
        return title;
    }

    @Override
    public Map<String, DTOCell> getCells() {
        return board;
    }

    @Override
    public int getNumOfRows() {
        return numOfRows;
    }

    @Override
    public int getNumOfColumns(){
        return numOfCols;
    }

    @Override
    public int getHeightOfRows() {
        return heightOfRows;
    }

    @Override
    public int getWidthOfColumns(){
        return widthOfCols;
    }
    /**
     * Adds a DTOCell to the board map using the cell's coordinate as the key.
     * @param dtoCell the DTOCell to add to the board.*/
    public void addDTOCell(DTOCell dtoCell) {
        DTOCoordinate dtoCoordinate = dtoCell.getCoordinate();
        board.put(dtoCoordinate.toString(), dtoCell);
    }

    public DTOCell getDTOCell(int col,int row) {
        char realCol = (char)(col + 'A' -1);
        DTOCoordinate dtoCoordinate = new DTOCoordinateImpl(row,realCol);
        String key = dtoCoordinate.toString();
        return this.board.get(key);
    }

    public void removeDTOCell(char col,int row) {
        DTOCoordinate dtoCoordinate = new DTOCoordinateImpl(row,col);
        String key = dtoCoordinate.toString();
        this.board.remove(key);

    }

}
