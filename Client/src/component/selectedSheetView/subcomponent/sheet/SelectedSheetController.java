package component.selectedSheetView.subcomponent.sheet;

import component.selectedSheetView.main.SelectedSheetViewController;
import component.selectedSheetView.subcomponent.sheetPoller.SheetPollerTask;
import dto.DTOCell;
import dto.DTOCoordinate;
import dto.DTOSheet;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Controller responsible for managing the display and interaction with the sheet.
 * This controller interacts with the main controller and manages the visual layout
 * and properties of the sheet, including cell customization such as column width,
 * row height, and cell colors.
 */
public class SelectedSheetController {

    private SelectedSheetViewController selectedSheetViewController;
    private UIModelSheet uiModel;
    private String selectedSheetName;

    @FXML
    private GridPane sheetGrid;

    /**
     * Constructor initializes the UIModelSheet for the controller.
     */
    public SelectedSheetController() {
        this.uiModel = new UIModelSheet();
    }

    /**
     * Initializes the controller and sets up references to the main controller.
     */
    public void initialize() {
        this.selectedSheetViewController = new SelectedSheetViewController();
    }

    /**
     * Sets the main controller reference for managing interactions between
     * different sections of the sheet view.
     *
     * @param selectedSheetViewController the main controller of the selected sheet view
     */
    public void setSelectedSheetViewController(SelectedSheetViewController selectedSheetViewController) {
        this.selectedSheetViewController = selectedSheetViewController;
    }

    /**
     * Sets the width of a specific column.
     *
     * @param colIndex the index of the column to set the width for
     * @param width the new width of the column
     */
    public void setColumnWidth(int colIndex, int width) {
        uiModel.setColumnWidth(colIndex, width);
    }

    /**
     * Sets the height of a specific row.
     *
     * @param rowIndex the index of the row to set the height for
     * @param height the new height of the row
     */
    public void setRowHeight(int rowIndex, int height) {
        uiModel.setRowHeight(rowIndex, height);
    }

    /**
     * Sets the background color of a specific cell.
     *
     * @param cellId the ID of the cell to set the background color for
     * @param backgroundColor the new background color
     */
    public void setCellBackgroundColor(String cellId, Color backgroundColor) {
        uiModel.setCellBackgroundColor(cellId, backgroundColor);
    }

    /**
     * Sets the text color of a specific cell.
     *
     * @param cellId the ID of the cell to set the text color for
     * @param textColor the new text color
     */
    public void setCellTextColor(String cellId, Color textColor) {
        uiModel.setCellTextColor(cellId, textColor);
    }

    /**
     * Sets the alignment of a specific column.
     *
     * @param colIndex the index of the column
     * @param alignment the new alignment for the column
     */
    public void setColumnAlignment(int colIndex, Pos alignment) {
        uiModel.setColumnAlignment(colIndex, alignment);
    }

    public void initSheetAndBindToUIModel(DTOSheet dtoSheet,String selectedSheetName ) {
        sheetGrid.getChildren().clear();

        int numOfRows = dtoSheet.getNumOfRows();
        int numOfCols = dtoSheet.getNumOfColumns();
        int widthOfCols = dtoSheet.getWidthOfColumns();
        int heightOfRows = dtoSheet.getHeightOfRows();

        // Initialize the UI model with the size of the sheet.
        uiModel.initializeModel(numOfRows + 1, numOfCols + 1, widthOfCols, heightOfRows);

        // Initialize column headers (A, B, C...)
        for (int col = 1; col <= numOfCols; col++) {
            String colLetter = String.valueOf((char) ('A' + col - 1));
            String cellKey = getCellId(col, 0);
            Label cellLabel = createLabel(widthOfCols, 15);
            uiModel.setCellValue(cellKey, colLetter);
            uiModel.bindCellToModel(cellLabel, cellKey);
            sheetGrid.add(cellLabel, col, 0);
        }

        // Initialize row headers (1, 2, 3...)
        for (int row = 1; row <= numOfRows; row++) {
            String cellKey = getCellId(0, row);
            Label cellLabel = createLabel(20, heightOfRows);
            uiModel.setCellValue(cellKey, String.valueOf(row));
            uiModel.bindCellToModel(cellLabel, cellKey);
            sheetGrid.add(cellLabel, 0, row);
        }

        // Initialize cells in the grid
        for (int row = 1; row <= numOfRows; row++) {
            for (int col = 1; col <= numOfCols; col++) {

                String cellKey = getCellId(col, row);
                Label cellLabel = createLabel(widthOfCols, heightOfRows);
                addClickEventForCell(cellLabel);

                DTOCell dtoCell = dtoSheet.getCells().get(cellKey);
                if (dtoCell != null) {
                    uiModel.setCellValue(cellKey, dtoCell.getEffectiveValue().toString());
                    uiModel.setCellOriginalValue(cellKey, dtoCell.getOriginalValue());
                    uiModel.setLastModifiedVersion(cellKey, dtoCell.getLastModifiedVersion());
                    List<String> DependsOnList = TurnDtoCoordinateListToCellIdList(dtoCell.getDependsOn());
                    uiModel.setCellDependsOn(cellKey, DependsOnList);
                    List<String> InfluencingOnList = TurnDtoCoordinateListToCellIdList(dtoCell.getInfluencingOn());
                    uiModel.setCellInfluencingOn(cellKey, InfluencingOnList);

                    //להוסיף ל username
                }

                uiModel.bindCellToModel(cellLabel, cellKey);
                sheetGrid.add(cellLabel, col, row);
            }
        }

        this.selectedSheetName = selectedSheetName;
    }

    /**
     * Updates the sheet values based on the DTO.
     * @param dtoSheet the DTO containing updated values.
     */
    public void updateSheetValues(DTOSheet dtoSheet) {
        Map<String, DTOCell> sheetMap = dtoSheet.getCells();
        for (DTOCell cell : sheetMap.values()) {
            uiModel.setCellValue(cell.getCoordinate().toString(), cell.getEffectiveValue());
            uiModel.setCellOriginalValue(cell.getCoordinate().toString(), cell.getOriginalValue());
            uiModel.setLastModifiedVersion(cell.getCoordinate().toString(), cell.getLastModifiedVersion());
            List<String> DependsOnList = TurnDtoCoordinateListToCellIdList(cell.getDependsOn());
            uiModel.setCellDependsOn(cell.getCoordinate().toString(), DependsOnList);
            List<String> InfluencingOnList = TurnDtoCoordinateListToCellIdList(cell.getInfluencingOn());
            uiModel.setCellInfluencingOn(cell.getCoordinate().toString(), InfluencingOnList);

            //להוסיף ל username
        }
        selectedSheetViewController.SelectSameCell(); // Call to select the same cell after update.
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
     * Creates a Label with the specified width and height.
     * @param width the preferred width of the label.
     * @param height the preferred height of the label.
     * @return the created label.
     */
    private Label createLabel(int width, int height) {
        Label label = new Label();
        label.setWrapText(true);
        label.setPrefSize(width, height);
        label.setMinSize(width, height);
        label.setMaxSize(width, height);
        return label;
    }

    /**
     * Returns the cell identifier based on the column and row index.
     * @param col the column index.
     * @param row the row index.
     * @return the cell identifier in the format "A:1", "B:2", etc.
     */
    private String getCellId(int col, int row) {
        char colLetter = (char) ('A' + (col - 1)); // Converts column index to a letter, e.g., 1 -> A
        return String.valueOf(colLetter) + ":" + row;
    }

    /**
     * Adds a click event listener to a label representing a cell.
     * When clicked, the corresponding cell is selected.
     * @param label the label (cell) to add the event listener to.
     */
    private void addClickEventForCell(Label label) {
        label.setOnMouseClicked(event -> {
            int row = GridPane.getRowIndex(label); // Get row index of the label
            int col = GridPane.getColumnIndex(label); // Get column index of the label
            String cellId = getCellId(col, row); // Get cell ID
            selectedSheetViewController.selectCell(cellId); // Select the cell via appController
        });
    }

    /**
     * Gets the values in a specific column between two rows.
     * @param column the column letter (e.g., 'A').
     * @param firstRow the first row index.
     * @param lastRow the last row index.
     * @return a list of values in the specified column between the rows.
     */
    public List<String> getColumnValues(char column, int firstRow, int lastRow) {
        List<String> columnValues = new ArrayList<>();
        for (int rowNum = firstRow; rowNum <= lastRow; rowNum++) {
            String cellId = getCellIdByChar(column, rowNum);
            String value = uiModel.getCell(cellId).valueProperty().getValue();
            if (!value.isEmpty()) {
                columnValues.add(value);
            }
        }
        return columnValues;
    }
    /**
     * Adds borders for the specified cells with the given styles.
     * @param color the color of the border.
     * @param style the style of the border.
     * @param width the width of the border.
     * @param cellsId the list of cell IDs to apply the border to.
     */
    public void addBorderForCells(Color color, String style, double width, List<String> cellsId) {
        for (String cellId : cellsId) {
            uiModel.setCellBorderColor(cellId, color);
            uiModel.setCellBorderStyle(cellId, style);
            uiModel.setCellBorderWidth(cellId, width);
        }
    }

    /**
     * Gets the text color of a specific cell.
     * @param cellId the ID of the cell.
     * @return the text color of the cell.
     */
    public Color getCellTextColor(String cellId) {
        return uiModel.getCellTextColor(cellId);
    }

    /**
     * Gets the background color of a specific cell.
     * @param cellId the ID of the cell.
     * @return the background color of the cell.
     */
    public Color getCellBackgroundColor(String cellId) {
        return uiModel.getCellBackgroundColor(cellId);
    }

    /**
     * Gets the height of a specific cell.
     * @param cellId the ID of the cell.
     * @return the height of the cell.
     */
    public int getCellHeight(String cellId) {
        return uiModel.getCellHeight(cellId);
    }

    /**
     * Gets the width of a specific cell.
     * @param cellId the ID of the cell.
     * @return the width of the cell.
     */
    public int getCellWidth(String cellId) {
        return uiModel.getCellWidth(cellId);
    }

    /**
     * Gets the alignment of a specific cell as a string.
     * @param cellId the ID of the cell.
     * @return the alignment as a string ("Center", "Left", or "Right").
     */
    public String getCellAligmentString(String cellId) {
        Pos alignment = uiModel.getCellAlignment(cellId);
        switch (alignment) {
            case CENTER:
                return "Center";
            case CENTER_LEFT:
                return "Left";
            case CENTER_RIGHT:
                return "Right";
            default:
                return alignment.toString();
        }
    }

    /**
     * Gets the original value of a specific cell.
     * @param cellId the ID of the cell.
     * @return the original value of the cell.
     */
    public String getOriginalValue(String cellId) {return uiModel.getCellOriginalValue(cellId);}

    /**
     * Gets the Last Modified Version of a specific cell.
     * @param cellId the ID of the cell.
     * @return the Last Modified Version of the cell.
     */
    public Integer getLastModifiedVersion(String cellId) {return uiModel.getLastModifiedVersion(cellId);}

    /**
     * Gets the cell ID by column letter and row number.
     * @param col the column letter.
     * @param row the row number.
     * @return the cell ID in the format "A:1", "B:2", etc.
     */
    private String getCellIdByChar(char col, int row) {
        return String.valueOf(col) + ":" + row;
    }
    public List<String> getDependsOn(String cellId) {return uiModel.getCellDependsOn(cellId);}

    public List<String> getInfluencingOn(String cellId) {return  uiModel.getCellInfluencingOn(cellId);}

    /**
     * Gets the current UI model of the sheet.
     * @return the current UI model.
     */
    public UIModelSheet getCurrentUIModel() {
        return uiModel;
    }
}
