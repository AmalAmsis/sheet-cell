package manager;

import dto.DTOSheet;
import dto.DTOCell;

public interface UIManager {
    DTOSheet getDtoSheetForDisplaySheet();
    DTOCell getDtoCellForDisplayCell();
    void updateCellValue();
    void loadSheetFromXmlFile(String filePath) throws Exception;
    void initSystem();
}
