package component.selectedSheetView.main;



import component.selectedSheetView.subcomponent.header.SelectedSheetViewHeaderController;
import component.selectedSheetView.subcomponent.left.SelectedSheetViewLeftController;
import component.selectedSheetView.subcomponent.sheet.SelectedSheetController;
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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is the main controller for the application.
 * It handles the interactions between different UI components (Header, Sheet, Left pane)
 * and manages the main logic for cell selection, data display, and range operations.
 */
public class SelectedSheetViewController {

    @FXML
    private ScrollPane header;
    @FXML
    private SelectedSheetViewHeaderController headerController;
    @FXML
    private ScrollPane sheet;
    @FXML
    private SelectedSheetController sheetController;
    @FXML
    private ScrollPane left;
    @FXML
    private SelectedSheetViewLeftController leftController;

    private Stage primaryStage;
    

    // Properties to track selected cell and range
    private ObjectProperty<String> selectedCellId = new SimpleObjectProperty<>();
    private ObjectProperty<String> selectedRangeId = new SimpleObjectProperty<>();

    // Boolean properties to manage the state of selection and file loading
    private BooleanProperty isSelected = new SimpleBooleanProperty();
    private BooleanProperty isFileLoaded = new SimpleBooleanProperty();
    private BooleanProperty isNumericCellSelected = new SimpleBooleanProperty();

    // List to store previously selected cells for clearing their state
    private List<String> previouslySelectedCells = new ArrayList<>();

   

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
//                DTOCell dtoCell = uiManager.getDtoCellForDisplayCell(newCellId.replace(":", ""));
//                isNumericCellSelected.setValue(isNumeric(dtoCell.getOriginalValue()));
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

    private void showRange(String newRangeId) {

    }

    private void clearPreviousSelection() {
    }

    private void showCellData(String newCellId) {
    }

    private void unShowCellData(String oldCellId) {
    }


    /**
     * Displays the loaded sheet in the UI.
     */
    public void displaySheet() {
        
    }


    public void setSelectedCellBackgroundColor(Color backgroundColor) {
    }

    public void setSelectedCellTextColor(Color textColor) {
    }

    public void setSelectedColumnWidth(Integer newVal) {
    }

    public void setSelectedRowHeight(Integer newVal) {
    }

    public void setSelectedColumnAlignment(Pos alignment) {
    }
}



