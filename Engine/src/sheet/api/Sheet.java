package sheet.api;

import sheet.cell.api.Cell;

public interface Sheet {
    int getVersion();
    String getTitle();
    Cell getCell(int row, int column);
    void setCell(int row, int column, String originalValue);
}
