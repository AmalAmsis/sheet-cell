package component.main.app;

import component.subcomponent.header.HeaderController;
import component.subcomponent.popup.errormessage.ErrorMessage;
import component.subcomponent.sheet.CellStyle;
import component.subcomponent.sheet.SheetController;
import component.subcomponent.left.LeftController;


import component.subcomponent.sheet.UIModelSheet;
import dto.DTOCell;
import dto.DTOCoordinate;
import dto.DTORange;
import dto.DTOSheet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import manager.UIManager;
import manager.UIManagerImpl;
import sheet.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AppController {

    @FXML
    private ScrollPane header;
    @FXML
    private HeaderController headerController;
    @FXML
    private ScrollPane sheet;
    @FXML
    private SheetController sheetController;
    @FXML
    private ScrollPane left;
    @FXML
    private LeftController leftController;

    private UIManager uiManager;
    private ObjectProperty<String> selectedCellId = new SimpleObjectProperty<>();
    private ObjectProperty<String> selectedRangeId = new SimpleObjectProperty<>();
    private BooleanProperty isSelected = new SimpleBooleanProperty();
    private BooleanProperty isFileLoaded = new SimpleBooleanProperty();

    // רשימה שמחזיקה את התאים שסומנו בעבר
    private List<String> previouslySelectedCells = new ArrayList<>();

    public AppController() {
        this.uiManager = new UIManagerImpl();
    }

    @FXML
    public void initialize() {
        if (headerController != null && sheetController != null && leftController != null) {
            headerController.setAppController(this);
            sheetController.setAppController(this);
            leftController.setAppController(this);
        }

        isSelected.setValue(false);
        isFileLoaded.setValue(false);
        headerController.bindingToIsSelected(isSelected);
        leftController.bindingToIsFileLoaded(isFileLoaded);

        // מאזין לשינויים בתא הנבחר
        selectedCellId.addListener((observable, oldCellId, newCellId) -> {
            if (oldCellId != null) {
                unShowCellData(oldCellId);  // ביטול סימון מהתא הישן
            }
            if (newCellId != null) {
                showCellData(newCellId);// סימון התא החדש
                isSelected.setValue(true);
            }
        });

        // מאזין לשינויים ב-Range הנבחר
        selectedRangeId.addListener((observable, oldRangeId, newRangeId) -> {
            if (oldRangeId != null) {
                clearPreviousSelection();  // ניקוי ה-Range הישן
            }
            if (newRangeId != null) {
                showRange(newRangeId);  // הצגת ה-Range החדש
                isSelected.setValue(false);
            }
        });

    }


    public void loadAndDisplaySheetFromXmlFile(String filePath) throws Exception {
        loadSheetFromFile(filePath);
        displaySheet();
        leftController.updateChoiceBoxes();
        isFileLoaded.setValue(true);
    }


    public void loadSheetFromFile(String filePath) throws Exception {
        // Load the sheet from the XML file
        uiManager.loadSheetFromXmlFile(filePath);

    }

    public void displaySheet() {
        DTOSheet dtoSheet = uiManager.getDtoSheetForDisplaySheet();
        sheetController.initSheetAndBindToUIModel(dtoSheet);
    }

    public void unShowCellData(String cellId) {
        DTOCell dtoCell = uiManager.getDtoCellForDisplayCell(cellId.replace(":",""));
        headerController.updateHeaderValues("", "","", CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue(), CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue(),sheetController.getCellWidth(selectedCellId.getValue()), sheetController.getCellHeight(selectedCellId.getValue()), sheetController.getCellAligmentString(selectedCellId.getValue()) );
        clearPreviousSelection();


//        List<DTOCoordinate> dtoCoordinateDependsOnCellsList = dtoCell.getDependsOn();
//        List<String> DependsOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateDependsOnCellsList);
//        sheetController.addBorderForCells(CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue(), CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue(), CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue(), DependsOnCellsId);
//
//        List<DTOCoordinate> dtoCoordinateInfluencingOnCellsList = dtoCell.getInfluencingOn();
//        List<String> InfluencingOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateInfluencingOnCellsList);
//        sheetController.addBorderForCells(CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue(), CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue(), CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue(), InfluencingOnCellsId);
    }

    public void showCellData(String cellId) {
        clearPreviousSelection();  // נקה את הסימון הקודם

        //String cellIdWith = cellId.substring(0, 1) + ":" + cellId.substring(1);
        List<String> selectedCell = new ArrayList<>();
        selectedCell.add(cellId);

        sheetController.addBorderForCells(
                CellStyle.SELECTED_CELL_BORDER_COLOR.getColorValue(),
                CellStyle.SELECTED_CELL_BORDER_STYLE.getStyleValue(),
                CellStyle.SELECTED_CELL_BORDER_WIDTH.getWidthValue(), selectedCell);


        // המשך עם הלוגיקה להצגת תא
        DTOCell dtoCell = uiManager.getDtoCellForDisplayCell(cellId.replace(":", ""));
        headerController.updateHeaderValues(cellId.replace(":",""), dtoCell.getOriginalValue(), String.valueOf(dtoCell.getLastModifiedVersion()), sheetController.getCellTextColor(selectedCellId.getValue()), sheetController.getCellBackgroundColor(selectedCellId.getValue()), sheetController.getCellWidth(selectedCellId.getValue()), sheetController.getCellHeight(selectedCellId.getValue()), sheetController.getCellAligmentString(selectedCellId.getValue()) );

        List<DTOCoordinate> dtoCoordinateDependsOnCellsList = dtoCell.getDependsOn();
        List<String> DependsOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateDependsOnCellsList);
        sheetController.addBorderForCells(
                CellStyle.DEPENDS_ON_CELL_BORDER_COLOR.getColorValue(),
                CellStyle.DEPENDS_ON_CELL_BORDER_STYLE.getStyleValue(),
                CellStyle.DEPENDS_ON_CELL_BORDER_WIDTH.getWidthValue(),
                DependsOnCellsId
        );

        List<DTOCoordinate> dtoCoordinateInfluencingOnCellsList = dtoCell.getInfluencingOn();
        List<String> InfluencingOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateInfluencingOnCellsList);
        sheetController.addBorderForCells(
                CellStyle.INFLUENCING_ON_CELL_BORDER_COLOR.getColorValue(),
                CellStyle.INFLUENCING_ON_CELL_BORDER_STYLE.getStyleValue(),
                CellStyle.INFLUENCING_ON_CELL_BORDER_WIDTH.getWidthValue(),
                InfluencingOnCellsId
        );

        // נוסיף את התאים המסומנים כעת לרשימה של התאים שנצטרך לנקות בעתיד
        previouslySelectedCells.add(cellId);
        previouslySelectedCells.addAll(DependsOnCellsId);
        previouslySelectedCells.addAll(InfluencingOnCellsId);
    }

    private List<String> TurnDtoCoordinateListToCellIdList(List<DTOCoordinate> dtoCoordinateList) {
        List<String> cellsId = new ArrayList<>();
        for (DTOCoordinate dtoCoordinate : dtoCoordinateList) {
            cellsId.add(dtoCoordinate.toString());
        }
        return cellsId;
    }

    public void updateCellValue(String newOriginalValue){
        try {
            //String CellId = sheetController.getSelectedCellId();
            DTOSheet dtoSheet = uiManager.updateCellValue(selectedCellId.getValue(), newOriginalValue);
            sheetController.UpdateSheetValues(dtoSheet);
            //displaySheet();//??????????????????????????????????????????
        }catch (Exception e){
            new ErrorMessage(e.getMessage());
        }


    }





    public void clearPreviousSelection() {
        if (!previouslySelectedCells.isEmpty()) {
            // מחזיר את התאים שסומנו למצבם הרגיל
            sheetController.addBorderForCells(
                    CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue(),
                    CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue(),
                    CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue(),
                    previouslySelectedCells
            );
            previouslySelectedCells.clear();  // נקה את הרשימה לאחר הסרת הסימון
        }
    }

    // שינוי התא הנבחר
    public void selectCell(String cellId) {
        selectedCellId.set(cellId);  // מפעיל את ה-Listener
    }

    public void SelectSameCell() {
        showCellData(selectedCellId.getValue());
    }

    // שינוי ה-Range הנבחר
    public void selectRange(String rangeId) {
        selectedRangeId.set(rangeId);  // מפעיל את ה-Listener
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


    public int getNumOfVersions() {
        return uiManager.getNumOfVersion();
    }

    public DTOSheet getSheetByVersion(int version) {
        return uiManager.getSheetInVersion(version);
    }

    public int getNumOfChangesInVersion(int version) {
        return uiManager.getNumOfChangesInVersion(version);
    }

    public void addNewRange(String rangeName, String from, String to) {
        try {
            uiManager.addNewRange(rangeName, from, to);
        }catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }
    }

    public void removeRange(String selectedRange) {
        try {
            selectedRangeId.set(selectedRange);
            uiManager.removeRange(selectedRange);
            clearPreviousSelection();
        }catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }

    }

    public void showRange(String selectedRange) {
        clearPreviousSelection();  // נקה את הסימון הקודם

        try {
            DTORange dtoRange = uiManager.getRange(selectedRange);
            List<DTOCoordinate> dtoCoordinateDependsOnCellsList = dtoRange.getCoordinates();
            List<String> cellsId = TurnDtoCoordinateListToCellIdList(dtoCoordinateDependsOnCellsList);
            sheetController.addBorderForCells(
                    CellStyle.RANGE_CELL_BORDER_COLOR.getColorValue(),
                    CellStyle.RANGE_CELL_BORDER_STYLE.getStyleValue(),
                    CellStyle.RANGE_CELL_BORDER_WIDTH.getWidthValue(),
                    cellsId
            );

            // עדכן את הרשימה עם התאים הנוכחיים
            previouslySelectedCells.addAll(cellsId);
        } catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }

    }

    public List<String> getAllRanges() {
        try {
            List<String> ranges = new ArrayList<>();
            List<DTORange> allRanges = uiManager.getAllRanges();
            for (DTORange range : allRanges) {
                ranges.add(range.getName());
                //+"  : "+ range.getTopLeftCoordinate().toString().replace(":", "") + "-" + range.getBottomRightCoordinate().toString().replace(":", ""));
            }
            return ranges;
        }catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }
        return null;
    }

    public UIModelSheet getCurrentUIModel() {
        return sheetController.getCurrentUIModel();
    }

    public DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities) throws Exception {
        return uiManager.getSortedSheet(from, to, listOfColumnsPriorities);
    }

    public DTOSheet getfilterSheet(Map<String, List<String>> selectedColumnValues, String from, String to) throws Exception {
        return uiManager.filterSheet(selectedColumnValues, from, to);
    }


        public void setSelectedCellBackgroundColor(Color backgroundColor) {
        sheetController.setCellBackgroundColor(selectedCellId.getValue(), backgroundColor);
    }

    public void setSelectedCellTextColor(Color textColor) {
        sheetController.setCellTextColor(selectedCellId.getValue(), textColor);
    }

    public void setSelectedColumnWidth(Integer newVal) {
        int colIndex = selectedCellId.getValue().charAt(0) - 'A' + 1;
        sheetController.setColumnWidth(colIndex, newVal);
    }

    public void setSelectedRowHeight(Integer newVal) {
        String[] parts = selectedCellId.getValue().split(":");
        int row = Integer.parseInt(parts[1]);
        sheetController.setRowHeight(row, newVal);
    }

    public void setSelectedColumnAlignment(Pos alignment) {
        int colIndex = selectedCellId.getValue().charAt(0) - 'A' + 1;
        sheetController.setColumnAlignment(colIndex, alignment);
    }


    public List<String> getColumnValues(char column, int firstRow, int lastRow) {
        return sheetController.getColumnValues(column, firstRow, lastRow);
    }


}