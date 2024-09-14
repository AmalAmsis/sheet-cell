package manager;

import dto.DTOSheet;
import dto.DTOCell;

public interface UIManager {
    DTOSheet getDtoSheetForDisplaySheet();
    DTOCell getDtoCellForDisplayCell(String cellId);
    DTOSheet updateCellValue(String coordinateString, String value);
    void loadSheetFromXmlFile(String filePath) throws Exception;
    void initSystem();
    int getNumOfVersion();
    DTOSheet getSheetInVersion(int version);
    int getNumOfChangesInVersion(int version);
}
