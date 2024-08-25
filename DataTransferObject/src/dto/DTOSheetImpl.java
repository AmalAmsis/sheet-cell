package dto;

import sheet.Sheet;
import sheet.SheetImpl;
import sheet.cell.Cell;

import java.util.HashMap;
import java.util.Map;

public class DTOSheetImpl implements DTOSheet {

    int version;
    String title;
    Map<String, DTOCell> board = new HashMap<>();
    int numOfRows;
    int numOfCols;
    int heightOfRows;
    int widthOfCols;

    public DTOSheetImpl(Sheet sheet) {
        this.version = sheet.getVersion();
        this.title = sheet.getTitle();
        this.numOfRows = sheet.getNumOfRows();
        this.numOfCols = sheet.getNumOfCols();
        this.heightOfRows = sheet.getHeightOfRows();
        this.widthOfCols = sheet.getWidthOfCols();

        //create DTOCell from original board of cells.
        for( Cell cell : sheet.getBoard().values()) {
            DTOCell dtoCell = new DTOCellImpl(cell);
            addDTOCell(dtoCell);
        }
    }


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

    public void addDTOCell(DTOCell dtoCell) {
        DTOCoordinate dtoCoordinate = dtoCell.getCoordinate();
        board.put(dtoCoordinate.toString(), dtoCell);
    }


}
