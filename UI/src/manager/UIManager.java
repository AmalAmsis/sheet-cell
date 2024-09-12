package manager;

import dto.DTOSheet;
import dto.DTOCell;

public interface UIManager {
    DTOSheet getDtoSheetForDisplaySheet();
    DTOCell getDtoCellForDisplayCell(String cellId);
    void updateCellValue();
    void loadSheetFromXmlFile(String filePath) throws Exception;
    void initSystem();
}
