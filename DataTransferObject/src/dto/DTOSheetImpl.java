package dto;

import java.util.HashMap;
import java.util.Map;

public class DTOSheetImpl implements DTOSheet {

    int version;
    String title;
    Map<String, DTOCell> board = new HashMap<>();

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
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void addCell(DTOCell cell) {
        DTOCoordinate coordinate = cell.getCoordinate();
        board.put(coordinate.toString(), cell);
    }
}
