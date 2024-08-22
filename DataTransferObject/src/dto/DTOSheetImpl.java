package dto;

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

    public DTOSheetImpl(SheetImpl sheetImpl) {
        this.version = sheetImpl.getVersion();
        this.title = sheetImpl.getTitle();
        this.numOfRows = sheetImpl.getNumOfRows();
        this.numOfCols = sheetImpl.getNumOfCols();
        this.heightOfRows = sheetImpl.getHeightOfRows();
        this.widthOfCols = sheetImpl.getWidthOfCols();

        //create DTOCell from original board of cells.
        for( Cell cell : sheetImpl.getBoard().values()) {
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
