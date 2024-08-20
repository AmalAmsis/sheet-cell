package dto;

import java.util.Map;

public interface DTOSheet {

    int getSheetVersion();
    String getSheetTitle();
    Map<String,DTOCell> getCells();
    int getNumOfRows();
    int getNumOfColumns();
    int getHeightOfRows();
    int getWidthOfColumns();

    void setVersion(int version);
    void setTitle(String title);
    void addDTOCell(DTOCell cell);
    void setNumOfRows(int NumOfRows);
    void setNumOfColumns(int NumOfColumns);
    void setHeightOfRows(int HeightOfRows);
    void setWidthOfColumns(int HeightOfColumns);



}
