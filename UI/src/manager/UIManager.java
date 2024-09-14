package manager;

import dto.DTORange;
import dto.DTOSheet;
import dto.DTOCell;

import java.util.List;

public interface UIManager {
    DTOSheet getDtoSheetForDisplaySheet();
    DTOCell getDtoCellForDisplayCell(String cellId);
    DTOSheet updateCellValue(String coordinateString, String value);
    void loadSheetFromXmlFile(String filePath) throws Exception;
    void initSystem();
    int getNumOfVersion();
    DTOSheet getSheetInVersion(int version);
    int getNumOfChangesInVersion(int version);
    void addNewRange(String rangeName , String from, String to) throws Exception;
    void removeRange(String rangeName) throws Exception;
    DTORange getRange(String rangeName) throws Exception;
    List<DTORange> getAllRanges() throws Exception;

}
