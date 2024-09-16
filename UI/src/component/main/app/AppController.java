package component.main.app;

import component.subcomponent.header.HeaderController;
import component.subcomponent.sheet.CellStyle;
import component.subcomponent.sheet.SheetController;
import component.subcomponent.left.LeftController;


import component.subcomponent.sheet.UIModelSheet;
import dto.DTOCell;
import dto.DTOCoordinate;
import dto.DTORange;
import dto.DTOSheet;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import manager.UIManager;
import manager.UIManagerImpl;
import sheet.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.List;


public class AppController {

    @FXML private ScrollPane header;
    @FXML private HeaderController headerController;
    @FXML private ScrollPane sheet;
    @FXML private SheetController sheetController;
    @FXML private ScrollPane left;
    @FXML private LeftController leftController;

    private UIManager uiManager;

    public AppController(){
        this.uiManager=new UIManagerImpl();
    }


    public void loadAndDisplaySheetFromXmlFile(String filePath) throws Exception {
        loadSheetFromFile(filePath);
        displaySheet();
        leftController.updateChoiceBoxes();
    }


    public void loadSheetFromFile(String filePath) throws Exception {
        // Load the sheet from the XML file
        uiManager.loadSheetFromXmlFile(filePath);

    }

    public void displaySheet(){
        DTOSheet dtoSheet = uiManager.getDtoSheetForDisplaySheet();
        sheetController.initSheetAndBindToUIModel(dtoSheet);
    }

    public void unShowCellData(String cellId) {
        DTOCell dtoCell = uiManager.getDtoCellForDisplayCell(cellId);
        headerController.updateLabels("", "","");

        List<DTOCoordinate> dtoCoordinateDependsOnCellsList = dtoCell.getDependsOn();
        List<String> DependsOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateDependsOnCellsList);
        sheetController.addBorderForCells(CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue(), CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue(), CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue(), DependsOnCellsId);

        List<DTOCoordinate> dtoCoordinateInfluencingOnCellsList = dtoCell.getInfluencingOn();
        List<String> InfluencingOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateInfluencingOnCellsList);
        sheetController.addBorderForCells(CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue(), CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue(), CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue(), InfluencingOnCellsId);
    }

    public void showCellData(String cellId) {
        DTOCell dtoCell = uiManager.getDtoCellForDisplayCell(cellId);
        headerController.updateLabels(cellId, dtoCell.getOriginalValue(), String.valueOf(dtoCell.getLastModifiedVersion()));

        List<DTOCoordinate> dtoCoordinateDependsOnCellsList = dtoCell.getDependsOn();
        List<String> DependsOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateDependsOnCellsList);
        sheetController.addBorderForCells(CellStyle.DEPENDS_ON_CELL_BORDER_COLOR.getColorValue(), CellStyle.DEPENDS_ON_CELL_BORDER_STYLE.getStyleValue(), CellStyle.DEPENDS_ON_CELL_BORDER_WIDTH.getWidthValue(), DependsOnCellsId);

        List<DTOCoordinate> dtoCoordinateInfluencingOnCellsList = dtoCell.getInfluencingOn();
        List<String> InfluencingOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateInfluencingOnCellsList);
        sheetController.addBorderForCells(CellStyle.INFLUENCING_ON_CELL_BORDER_COLOR.getColorValue(), CellStyle.INFLUENCING_ON_CELL_BORDER_STYLE.getStyleValue(), CellStyle.INFLUENCING_ON_CELL_BORDER_WIDTH.getWidthValue(), InfluencingOnCellsId);

    }

    private List<String> TurnDtoCoordinateListToCellIdList(List<DTOCoordinate> dtoCoordinateList) {
        List<String> cellsId = new ArrayList<>();
        for (DTOCoordinate dtoCoordinate : dtoCoordinateList) {
            cellsId.add(dtoCoordinate.toString());
        }
        return cellsId;
    }

    public void updateCellValue(String newOriginalValue){
        String CellId = sheetController.getSelectedCellId();
        DTOSheet dtoSheet= uiManager.updateCellValue(CellId,newOriginalValue);
        sheetController.UpdateSheetValues(dtoSheet);
        //displaySheet();//??????????????????????????????????????????
    }



    @FXML
    public void initialize() {
       if(headerController != null && sheetController != null && leftController != null) {
            headerController.setAppController(this);
            sheetController.setAppController(this);
            leftController.setAppController(this);
        }

    }

    public void setHeaderController(HeaderController headerController) {
        this.headerController = headerController;
        headerController.setAppController(this);
    }

    public void setSheetController(SheetController sheetController) {
        this.sheetController = sheetController;
        sheetController.setAppController(this);
    }

    public void setLeftController(LeftController leftController) {
        this.leftController = leftController;
        leftController.setAppController(this);
    }


    public int getNumOfVersions(){
        return uiManager.getNumOfVersion();
    }

    public DTOSheet getSheetByVersion(int version) {
        return uiManager.getSheetInVersion(version);
    }

    public int getNumOfChangesInVersion(int version) {
        return uiManager.getNumOfChangesInVersion(version);
    }

    public void addNewRange(String rangeName,String from,String to) {
        try {
            uiManager.addNewRange(rangeName, from, to);
        }catch (Exception e) {
            // popup
        }
    }
    public void removeRange(String selectedRange){
        try {
            uiManager.removeRange(selectedRange);
        }catch (Exception e) {
            // popup
        }

    }
    public void showRange(String selectedRange){
        try {
            DTORange dtoRange = uiManager.getRange(selectedRange);
            List<DTOCoordinate> dtoCoordinateDependsOnCellsList = dtoRange.getCoordinates();
            List <String> cellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateDependsOnCellsList);
            sheetController.addBorderForCells(CellStyle.RANGE_CELL_BORDER_COLOR.getColorValue(), CellStyle.RANGE_CELL_BORDER_STYLE.getStyleValue(), CellStyle.RANGE_CELL_BORDER_WIDTH.getWidthValue(), cellsId);
        }catch (Exception e){
            //popup
        }

    }

    public List<String> getAllRanges(){
        try {
            List<String> ranges = new ArrayList<>();
            List<DTORange> allRanges = uiManager.getAllRanges();
            for (DTORange range : allRanges) {
                ranges.add(range.getName() +"  : "+ range.getTopLeftCoordinate().toString().replace(":", "") + "-" + range.getBottomRightCoordinate().toString().replace(":", ""));
            }
            return ranges;
        }catch (Exception e) {
            //POPUP
        }
        return null;
    }

    public UIModelSheet getCurrentUIModel() {
        return sheetController.getCurrentUIModel();
    }

    public DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities) throws Exception {
        return uiManager.getSortedSheet( from,  to,  listOfColumnsPriorities);
    }
}


