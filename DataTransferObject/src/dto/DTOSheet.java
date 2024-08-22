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


}
