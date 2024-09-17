package manager;

import dto.DTOCell;
import dto.DTORange;
import dto.DTOSheet;
import engine.Engine;
import engine.EngineImpl;
import sheet.version.SheetVersionData;
import sheet.version.SheetVersionHandler;

import java.util.List;

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
            return getDtoSheetForDisplaySheet();
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public void loadSheetFromXmlFile(String filePath) throws Exception {
        engine.loadSheetFromXmlFile(filePath);
    }

    //todo
    @Override
    public void initSystem() {

    }

    @Override
    public int getNumOfVersion(){
        return engine.getCurrentSheetState().getVersionHandler().getNumOfVersions();
    }

    @Override
    public DTOSheet getSheetInVersion(int version){
        return engine.getCurrentSheetState().getVersionHandler().getSheetByVersion(version);
    }

    @Override
    public int getNumOfChangesInVersion(int version){
       return engine.getCurrentSheetState().getVersionHandler().getVersionHistory().get(version).getNumOfUpdateCells();
    }

    @Override
    public void addNewRange(String rangeName, String from, String to) throws Exception {
        engine.addNewRange(rangeName, from, to);
    }

    @Override
    public void removeRange(String rangeName) throws Exception {
        engine.removeRange(rangeName);
    }

    @Override
    public DTORange getRange(String rangeName) throws Exception {
        return engine.getRange(rangeName);
    }

    @Override
    public List<DTORange> getAllRanges() throws Exception {
        return engine.getAllRanges();
    }


}
