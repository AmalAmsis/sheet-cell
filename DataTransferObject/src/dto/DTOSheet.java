package dto;

import java.util.Map;

public interface DTOSheet {

    int getSheetVersion();
    String getSheetTitle();
    Map<String,DTOCell> getCells();

    void setVersion(int version);
    void setTitle(String title);
    void addCell(DTOCell cell);

}
