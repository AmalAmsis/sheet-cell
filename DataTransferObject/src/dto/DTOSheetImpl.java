package dto;

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

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void addDTOCell(DTOCell cell) {
        DTOCoordinate coordinate = cell.getCoordinate();
        board.put(coordinate.toString(), cell);
    }

    @Override
    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    @Override
    public void setNumOfColumns(int NumOfColumns) {
        this.numOfCols = NumOfColumns;
    }

    @Override
    public void setHeightOfRows(int HeightOfRows) {
        this.heightOfRows = HeightOfRows;
    }

    @Override
    public void setWidthOfColumns(int WidthOfColumns) {
        this.widthOfCols = WidthOfColumns;
    }


}
