package manager;

import dto.DTOCell;
import dto.DTOSheet;
import engine.Engine;
import engine.EngineImpl;

public class UIManagerImpl implements UIManager {

    private Engine engine;


    /**
     * UIManagerImpl constructor
     */
    public UIManagerImpl() {
        this.engine = new EngineImpl();
    }

    @Override
    public DTOSheet getDtoSheetForDisplaySheet() {
        return engine.displaySheet();
    }

    //todo
    @Override
    public DTOCell getDtoCellForDisplayCell(String cellId) {
        return engine.displayCell(cellId);
    }

    //todo
    @Override
    public DTOSheet updateCellValue(String coordinateString, String value) {
        try {
            engine.updateCell(coordinateString, value);
            DTOSheet dtoSheet = getDtoSheetForDisplaySheet();
            return dtoSheet;
        }
        catch (Exception e) {

        }
        return null;
    }

    @Override
    public void loadSheetFromXmlFile(String filePath) throws Exception {
        engine.loadSheetFromXmlFile(filePath);
    }

    //todo
    @Override
    public void initSystem() {

    }
}
