package component.selectedSheetView.subcomponent.sheet;

import component.selectedSheetView.main.SelectedSheetViewController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Controller responsible for managing the display and interaction with the sheet.
 * This controller interacts with the main controller and manages the visual layout
 * and properties of the sheet, including cell customization such as column width,
 * row height, and cell colors.
 */
public class SelectedSheetController {

    private SelectedSheetViewController selectedSheetViewController;
    private UIModelSheet uiModel;

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

}
