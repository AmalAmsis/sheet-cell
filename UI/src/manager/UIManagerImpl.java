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
    public DTOCell getDtoCellForDisplayCell() {

        return null;
    }

    //todo
    @Override
    public void updateCellValue() {

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
