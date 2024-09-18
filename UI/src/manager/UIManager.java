package manager;

import dto.DTORange;
import dto.DTOSheet;
import dto.DTOCell;
import sheet.coordinate.Coordinate;

import java.util.List;
import java.util.Map;

public interface UIManager {
    DTOSheet getDtoSheetForDisplaySheet();
    DTOCell getDtoCellForDisplayCell(String cellId);
    DTOSheet updateCellValue(String coordinateString, String value) throws Exception;
    void loadSheetFromXmlFile(String filePath) throws Exception;
    void initSystem();
    int getNumOfVersion();
    DTOSheet getSheetInVersion(int version);
    int getNumOfChangesInVersion(int version);
    void addNewRange(String rangeName , String from, String to) throws Exception;
    void removeRange(String rangeName) throws Exception;
    DTORange getRange(String rangeName) throws Exception;
    List<DTORange> getAllRanges() throws Exception;

    DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities) throws Exception ;
    DTOSheet filterSheet(Map<String, List<String>> selectedColumnValues, String from, String to) throws Exception;

    }
