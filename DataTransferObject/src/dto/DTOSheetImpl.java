package dto;

import sheet.Sheet;
import sheet.SheetImpl;
import sheet.cell.Cell;
import sheet.range.Range;

import java.io.Serializable;
import java.util.HashMap;
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

    //**********************************************YARDEN**********************************************//


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

}
