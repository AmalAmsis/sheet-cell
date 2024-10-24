package manager;

import dto.DTOCell;
import dto.DTORange;
import dto.DTOSheet;
import sheetmanager.SheetManager;
import sheetmanager.SheetManagerImpl;

import java.util.List;
import java.util.Map;

public class UIManagerImpl implements UIManager {

    private SheetManager sheetManager;


    /**
     * UIManagerImpl constructor
     */
    public UIManagerImpl() {
        this.sheetManager = new SheetManagerImpl();
    }

    @Override
    public DTOSheet getDtoSheetForDisplaySheet() {
        return sheetManager.displaySheet();
    }

    //todo
    @Override
    public DTOCell getDtoCellForDisplayCell(String cellId) {
        return sheetManager.displayCell(cellId);
    }

    //todo
    @Override
    public DTOSheet updateCellValue(String coordinateString, String value) throws Exception {
        sheetManager.updateCell(coordinateString, value,"");
        return getDtoSheetForDisplaySheet();
    }

    @Override
    public DTOSheet updateTemporaryCellValue(String coordinateString, String newOriginalValue)  {

        DTOSheet sheet = sheetManager.updateTemporaryCellValue(coordinateString, newOriginalValue);
        return sheet;

    }

    @Override
    public void loadSheetFromXmlFile(String filePath) throws Exception {
        sheetManager.loadSheetFromXmlFile(filePath);
    }

    //todo
    @Override
    public void initSystem() {

    }

    @Override
    public int getNumOfVersion() {
        return sheetManager.getCurrentSheetState().getVersionHandler().getNumOfVersions();
    }

    @Override
    public DTOSheet getSheetInVersion(int version) {
        return sheetManager.getCurrentSheetState().getVersionHandler().getSheetByVersion(version);
    }

    @Override
    public int getNumOfChangesInVersion(int version) {
        return sheetManager.getCurrentSheetState().getVersionHandler().getVersionHistory().get(version).getNumOfUpdateCells();
    }

    @Override
    public void addNewRange(String rangeName, String from, String to) throws Exception {
        sheetManager.addNewRange(rangeName, from, to);
    }

    @Override
    public void removeRange(String rangeName) throws Exception {
        sheetManager.removeRange(rangeName);
    }

    @Override
    public DTORange getRange(String rangeName) throws Exception {
        return sheetManager.getRange(rangeName);
    }

    @Override
    public List<DTORange> getAllRanges() throws Exception {
        return sheetManager.getAllRanges();
    }


    @Override
    public DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities) throws Exception {
        return sheetManager.getSortedSheet(from, to, listOfColumnsPriorities);
    }

    @Override
    public DTOSheet filterSheet(Map<String, List<String>> selectedColumnValues, String from, String to) throws Exception {
        return sheetManager.filterSheet(selectedColumnValues, from, to);
    }
}
