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
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import manager.UIManager;
import manager.UIManagerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sun.javafx.application.PlatformImpl.runLater;

/**
 * This class is the main controller for the application.
 * It handles the interactions between different UI components (Header, Sheet, Left pane)
 * and manages the main logic for cell selection, data display, and range operations.
 */
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

    private Stage primaryStage;
    private UIManager uiManager;

    // Properties to track selected cell and range
    private ObjectProperty<String> selectedCellId = new SimpleObjectProperty<>();
    private ObjectProperty<String> selectedRangeId = new SimpleObjectProperty<>();

    // Boolean properties to manage the state of selection and file loading
    private BooleanProperty isSelected = new SimpleBooleanProperty();
    private BooleanProperty isFileLoaded = new SimpleBooleanProperty();
    private BooleanProperty isNumericCellSelected = new SimpleBooleanProperty();

    // List to store previously selected cells for clearing their state
    private List<String> previouslySelectedCells = new ArrayList<>();

    // Constructor
    public AppController() {
        this.uiManager = new UIManagerImpl();
    }

    @FXML
    public void initialize() {
        // Ensure that the sub-controllers are set up properly
        if (headerController != null && sheetController != null && leftController != null) {
            headerController.setAppController(this);
            sheetController.setAppController(this);
            leftController.setAppController(this);
        }

        // Initialize properties
        isSelected.setValue(false);
        isFileLoaded.setValue(false);
        isNumericCellSelected.setValue(false);

        // Bind properties to UI components
        headerController.bindingToIsSelected(isSelected);
        headerController.bindingToIsFileLoaded(isFileLoaded);
        leftController.bindingToIsFileLoaded(isFileLoaded);
        leftController.bindingToNumericCellIsSelected(isNumericCellSelected);

        // Listener for selected cell changes
        selectedCellId.addListener((observable, oldCellId, newCellId) -> {
            if (oldCellId != null) {
                unShowCellData(oldCellId);  // Unselect old cell
            }
            if (newCellId != null) {
                showCellData(newCellId);  // Select new cell
                isSelected.setValue(true);

                // Check if the cell contains a numeric value
                DTOCell dtoCell = uiManager.getDtoCellForDisplayCell(newCellId.replace(":", ""));
                isNumericCellSelected.setValue(isNumeric(dtoCell.getOriginalValue()));
            }
        });

        // Listener for selected range changes
        selectedRangeId.addListener((observable, oldRangeId, newRangeId) -> {
            if (oldRangeId != null) {
                clearPreviousSelection();  // Clear old range
            }
            if (newRangeId != null) {
                showRange(newRangeId);  // Show new range
                isSelected.setValue(false);
                isNumericCellSelected.setValue(false);
            }
        });
    }

    /**
     * Loads the sheet from an XML file and displays it.
     * @param filePath the path to the XML file.
     * @throws Exception if there is an error during loading.
     */
    public void loadAndDisplaySheetFromXmlFile(String filePath) throws Exception {
        // Load the sheet from the file
        loadSheetFromFile(filePath);

        // Use Platform.runLater to ensure the UI updates are done on the JavaFX Application Thread
        Platform.runLater(() -> {
            displaySheet();  // Update the display with the loaded sheet
            leftController.updateChoiceBoxes();  // Update any necessary UI components (like choice boxes)
            isFileLoaded.setValue(true);  // Indicate that the file is successfully loaded
        });
    }

    /**
     * Loads the sheet data from the specified XML file.
     * @param filePath the path to the XML file.
     * @throws Exception if there is an error during loading.
     */
    public void loadSheetFromFile(String filePath) throws Exception {
        uiManager.loadSheetFromXmlFile(filePath);
    }

    /**
     * Displays the loaded sheet in the UI.
     */
    public void displaySheet() {
        DTOSheet dtoSheet = uiManager.getDtoSheetForDisplaySheet();
        sheetController.initSheetAndBindToUIModel(dtoSheet);
    }

    /**
     * Unselects the given cell and clears its data from the header.
     * @param cellId the ID of the cell to unselect.
     */
    public void unShowCellData(String cellId) {
        DTOCell dtoCell = uiManager.getDtoCellForDisplayCell(cellId.replace(":", ""));
        headerController.updateHeaderValues("", "", "",
                CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue(),
                CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue(),
                sheetController.getCellWidth(selectedCellId.getValue()),
                sheetController.getCellHeight(selectedCellId.getValue()),
                sheetController.getCellAligmentString(selectedCellId.getValue()));

        clearPreviousSelection();
    }

    /**
     * Selects and shows data for the given cell.
     * @param cellId the ID of the cell to select.
     */
    public void showCellData(String cellId) {
        clearPreviousSelection();

        List<String> selectedCell = new ArrayList<>();
        selectedCell.add(cellId);

        sheetController.addBorderForCells(
                CellStyle.SELECTED_CELL_BORDER_COLOR.getColorValue(),
                CellStyle.SELECTED_CELL_BORDER_STYLE.getStyleValue(),
                CellStyle.SELECTED_CELL_BORDER_WIDTH.getWidthValue(), selectedCell);

        DTOCell dtoCell = uiManager.getDtoCellForDisplayCell(cellId.replace(":", ""));
        headerController.updateHeaderValues(cellId.replace(":", ""), dtoCell.getOriginalValue(),
                String.valueOf(dtoCell.getLastModifiedVersion()),
                sheetController.getCellTextColor(selectedCellId.getValue()),
                sheetController.getCellBackgroundColor(selectedCellId.getValue()),
                sheetController.getCellWidth(selectedCellId.getValue()),
                sheetController.getCellHeight(selectedCellId.getValue()),
                sheetController.getCellAligmentString(selectedCellId.getValue()));

        // Highlight dependencies and influenced cells
        List<String> DependsOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCell.getDependsOn());
        sheetController.addBorderForCells(CellStyle.DEPENDS_ON_CELL_BORDER_COLOR.getColorValue(),
                CellStyle.DEPENDS_ON_CELL_BORDER_STYLE.getStyleValue(),
                CellStyle.DEPENDS_ON_CELL_BORDER_WIDTH.getWidthValue(), DependsOnCellsId);

        List<String> InfluencingOnCellsId = TurnDtoCoordinateListToCellIdList(dtoCell.getInfluencingOn());
        sheetController.addBorderForCells(CellStyle.INFLUENCING_ON_CELL_BORDER_COLOR.getColorValue(),
                CellStyle.INFLUENCING_ON_CELL_BORDER_STYLE.getStyleValue(),
                CellStyle.INFLUENCING_ON_CELL_BORDER_WIDTH.getWidthValue(), InfluencingOnCellsId);

        // Add the cells to the list for clearing later
        previouslySelectedCells.add(cellId);
        previouslySelectedCells.addAll(DependsOnCellsId);
        previouslySelectedCells.addAll(InfluencingOnCellsId);
    }

    // Helper method to convert DTOCoordinate list to cell ID list
    private List<String> TurnDtoCoordinateListToCellIdList(List<DTOCoordinate> dtoCoordinateList) {
        List<String> cellsId = new ArrayList<>();
        for (DTOCoordinate dtoCoordinate : dtoCoordinateList) {
            cellsId.add(dtoCoordinate.toString());
        }
        return cellsId;
    }

    /**
     * Updates the value of the selected cell.
     * @param newOriginalValue the new value to set.
     */
    public void updateCellValue(String newOriginalValue) {
        try {
            DTOSheet dtoSheet = uiManager.updateCellValue(selectedCellId.getValue(), newOriginalValue);
            sheetController.updateSheetValues(dtoSheet);
        } catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }
    }

    // Clears the previous selection of cells
    public void clearPreviousSelection() {
        if (!previouslySelectedCells.isEmpty()) {
            sheetController.addBorderForCells(CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue(),
                    CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue(),
                    CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue(),
                    previouslySelectedCells);
            previouslySelectedCells.clear();
        }
    }

    // More methods...

    /**
     * Changes the selected cell and triggers the listener for cell selection.
     * @param cellId the ID of the cell to select.
     */
    public void selectCell(String cellId) {
        selectedCellId.set(cellId);  // Triggers the listener
    }

    /**
     * Selects the same cell again, mainly for re-displaying its data.
     */
    public void SelectSameCell() {
        showCellData(selectedCellId.getValue());
    }

    /**
     * Changes the selected range and triggers the listener for range selection.
     * @param rangeId the ID of the range to select.
     */
    public void selectRange(String rangeId) {
        selectedRangeId.set(rangeId);  // Triggers the listener
    }

    // Methods for binding controllers
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

    /**
     * Gets the number of versions in the current sheet.
     * @return the number of versions.
     */
    public int getNumOfVersions() {
        return uiManager.getNumOfVersion();
    }

    /**
     * Fetches a specific sheet version by version number.
     * @param version the version number.
     * @return the corresponding sheet.
     */
    public DTOSheet getSheetByVersion(int version) {
        return uiManager.getSheetInVersion(version);
    }

    /**
     * Gets the number of changes made in a specific version.
     * @param version the version number.
     * @return the number of changes in that version.
     */
    public int getNumOfChangesInVersion(int version) {
        return uiManager.getNumOfChangesInVersion(version);
    }

    /**
     * Adds a new range to the sheet based on the given parameters.
     * @param rangeName the name of the range.
     * @param from the starting cell of the range.
     * @param to the ending cell of the range.
     */
    public void addNewRange(String rangeName, String from, String to) {
        try {
            uiManager.addNewRange(rangeName, from, to);
        } catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }
    }

    /**
     * Removes a selected range from the sheet.
     * @param selectedRange the name of the range to remove.
     */
    public void removeRange(String selectedRange) {
        try {
            selectedRangeId.set(selectedRange);
            uiManager.removeRange(selectedRange);
            clearPreviousSelection();
        } catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }
    }

    /**
     * Highlights and displays the selected range.
     * @param selectedRange the name of the range to display.
     */
    public void showRange(String selectedRange) {
        clearPreviousSelection();
        try {
            DTORange dtoRange = uiManager.getRange(selectedRange);
            List<String> cellsId = TurnDtoCoordinateListToCellIdList(dtoRange.getCoordinates());
            sheetController.addBorderForCells(
                    CellStyle.RANGE_CELL_BORDER_COLOR.getColorValue(),
                    CellStyle.RANGE_CELL_BORDER_STYLE.getStyleValue(),
                    CellStyle.RANGE_CELL_BORDER_WIDTH.getWidthValue(),
                    cellsId);
            previouslySelectedCells.addAll(cellsId);
        } catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }
    }

    /**
     * Returns a list of all ranges in the sheet.
     * @return a list of all ranges.
     */
    public List<String> getAllRanges() {
        try {
            List<String> ranges = new ArrayList<>();
            List<DTORange> allRanges = uiManager.getAllRanges();
            for (DTORange range : allRanges) {
                ranges.add(range.getName());
            }
            return ranges;
        } catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }
        return null;
    }

    /**
     * Returns the current UI model sheet for display.
     * @return the current UIModelSheet object.
     */
    public UIModelSheet getCurrentUIModel() {
        return sheetController.getCurrentUIModel();
    }

    /**
     * Sorts the sheet based on specified columns and range.
     * @param from the starting cell.
     * @param to the ending cell.
     * @param listOfColumnsPriorities the columns to sort by.
     * @return the sorted sheet.
     * @throws Exception if an error occurs during sorting.
     */
    public DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities) throws Exception {
        return uiManager.getSortedSheet(from, to, listOfColumnsPriorities);
    }

    /**
     * Filters the sheet based on selected column values and range.
     * @param selectedColumnValues the selected column values.
     * @param from the starting cell.
     * @param to the ending cell.
     * @return the filtered sheet.
     * @throws Exception if an error occurs during filtering.
     */
    public DTOSheet getfilterSheet(Map<String, List<String>> selectedColumnValues, String from, String to) throws Exception {
        return uiManager.filterSheet(selectedColumnValues, from, to);
    }

    /**
     * Updates the background color of the selected cell.
     * @param backgroundColor the new background color.
     */
    public void setSelectedCellBackgroundColor(Color backgroundColor) {
        sheetController.setCellBackgroundColor(selectedCellId.getValue(), backgroundColor);
    }

    /**
     * Updates the text color of the selected cell.
     * @param textColor the new text color.
     */
    public void setSelectedCellTextColor(Color textColor) {
        sheetController.setCellTextColor(selectedCellId.getValue(), textColor);
    }

    /**
     * Updates the width of the selected column.
     * @param newVal the new width value.
     */
    public void setSelectedColumnWidth(Integer newVal) {
        int colIndex = selectedCellId.getValue().charAt(0) - 'A' + 1;
        sheetController.setColumnWidth(colIndex, newVal);
    }

    /**
     * Updates the height of the selected row.
     * @param newVal the new height value.
     */
    public void setSelectedRowHeight(Integer newVal) {
        String[] parts = selectedCellId.getValue().split(":");
        int row = Integer.parseInt(parts[1]);
        sheetController.setRowHeight(row, newVal);
    }

    /**
     * Updates the text alignment of the selected column.
     * @param alignment the new text alignment.
     */
    public void setSelectedColumnAlignment(Pos alignment) {
        int colIndex = selectedCellId.getValue().charAt(0) - 'A' + 1;
        sheetController.setColumnAlignment(colIndex, alignment);
    }

    /**
     * Retrieves the values of the specified column in the given range.
     * @param column the column to retrieve values from.
     * @param firstRow the first row in the range.
     * @param lastRow the last row in the range.
     * @return a list of values from the specified column.
     */
    public List<String> getColumnValues(char column, int firstRow, int lastRow) {
        return sheetController.getColumnValues(column, firstRow, lastRow);
    }

    /**
     * Returns the currently selected cell key.
     * @return the selected cell key.
     */
    public String getSelectedCellKey() {
        return selectedCellId.getValue();
    }

    /**
     * Returns the current sheet DTO object for display.
     * @return the current DTOSheet object.
     */
    public DTOSheet getCurrentSheetDTO() {
        return uiManager.getDtoSheetForDisplaySheet();
    }

    /**
     * Temporarily updates the value of a cell for dynamic analysis.
     * @param cellKey the key of the cell to update.
     * @param newValue the new value to set.
     * @return the updated DTOSheet object.
     */
    public DTOSheet updateTemporaryCellValue(String cellKey, double newValue) {
        return uiManager.updateTemporaryCellValue(cellKey, String.valueOf(newValue));
    }

    /**
     * Checks if a string contains a numeric value.
     * @param str the string to check.
     * @return true if the string is numeric, false otherwise.
     */
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sets the primary stage for the application.
     * @param stage the primary stage.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Applies a theme (stylesheet) to the application.
     * @param theme the name of the theme to apply.
     */
    public void applyTheme(String theme) {
        Scene scene = primaryStage.getScene();
        scene.getStylesheets().clear();

        if ("Style 1".equals(theme)) {
            String leftCssFile = getClass().getResource("/component/subcomponent/left/left.css").toExternalForm();
            String headerCssFile = getClass().getResource("/component/subcomponent/header/header.css").toExternalForm();
            scene.getStylesheets().add(leftCssFile);
            scene.getStylesheets().add(headerCssFile);
        } else {
            String leftCssFile = getClass().getResource("/component/subcomponent/left/secondLeft.css").toExternalForm();
            String headerCssFile = getClass().getResource("/component/subcomponent/header/secondHeader.css").toExternalForm();
            String singleCellCssFile = getClass().getResource("/component/subcomponent/sheet/single-cell.css").toExternalForm();
            scene.getStylesheets().add(leftCssFile);
            scene.getStylesheets().add(headerCssFile);
            scene.getStylesheets().add(singleCellCssFile);
        }
    }

    public List<String> getColumnValuesInRange(String range) throws IllegalArgumentException {
        List<String> values = new ArrayList<>();
        range = range.trim().toUpperCase();

        // בדיקת פורמט הטווח
        if (!range.matches("[A-Z]\\d+\\.\\.[A-Z]\\d+")) {
            throw new IllegalArgumentException("Invalid range format. Please use the correct format like A1..A10, where the start and end of the range are valid cell references. The range should be from the same column for each axis, and both the start and end cells must be within the bounds of the sheet.");
        }

        // נפרק את הטווח לדוגמה: "A1..A10" -> [A, 1] ו- [A, 10]
        String[] parts = range.split("\\.\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format. Please use the correct format like A1..A10, where the start and end of the range are valid cell references. The range should be from the same column for each axis, and both the start and end cells must be within the bounds of the sheet.");
        }

        String startCell = parts[0];
        String endCell = parts[1];

        // חילוץ עמודות ושורות מהתאים
        char startColumn = startCell.charAt(0);  // עמודה ראשונה
        char endColumn = endCell.charAt(0);  // עמודה אחרונה
        int startRow = Integer.parseInt(startCell.substring(1));  // שורה ראשונה
        int endRow = Integer.parseInt(endCell.substring(1));  // שורה אחרונה

        // נוודא שהעמודות תואמות (לדוגמה אם יש A1..A10)
        if (startColumn != endColumn) {
            throw new IllegalArgumentException("This method only supports ranges within the same column.");
        }

        // נשיג את ערכי התאים בעמודה הזו בטווח שורות זה
        for (int row = startRow; row <= endRow; row++) {
            String cellId = startColumn + ":" + row; // ניצור מזהה תא
            cellId = cellId.replace(":","");
            DTOCell cell = uiManager.getDtoCellForDisplayCell(cellId); // נשיג את תא דרך המודל

            if (cell != null) {
                String cellValue = cell.getEffectiveValue().toString();
                if (isNumeric(cellValue)) {  // נבדוק אם הערך מספרי
                    values.add(cellValue);  // המרה למספר והוספה לרשימה
                } else {
                    throw new IllegalArgumentException("Non-numeric value found in cell " + cellId + ": " + cellValue);
                }
            }
        }

        return values;
    }



}



