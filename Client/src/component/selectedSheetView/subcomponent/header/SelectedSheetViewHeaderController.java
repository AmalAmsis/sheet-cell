package component.selectedSheetView.subcomponent.header;

import component.selectedSheetView.main.SelectedSheetViewController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

/**
 * Controller for the header section of the Selected Sheet View.
 * Manages user interactions with controls such as color pickers, spinners, and buttons
 * for updating cell properties and managing the selected sheet.
 */
public class SelectedSheetViewHeaderController {

    private SelectedSheetViewController selectedSheetViewController;

    // Change listeners for spinners controlling column width and row height
    private ChangeListener<Integer> columnWidthListener;
    private ChangeListener<Integer> rowHeightListener;

    @FXML private Button BackToDefaultButton;
    @FXML private TextField actionLineTextField;
    @FXML private ChoiceBox<String> alignmentChoiceBox;
    @FXML private ColorPicker cellBackgroundColorPicker;
    @FXML private Label cellIdLabel;
    @FXML private Spinner<Integer> columnWidthSpinner;
    @FXML private Label filePathLabel;
    @FXML private Label lastModifiedVersionLabel;
    @FXML private Label originalValueLabel;
    @FXML private Spinner<Integer> rowHeightSpinner;
    @FXML private ColorPicker textColorPicker;
    @FXML private MenuButton themesMenuButton;
    @FXML private Button updateValueButton;
    @FXML private Button versionSelectorButton;
    @FXML private Button backToDashBoardButton;

    /**
     * Constructor for the SelectedSheetViewHeaderController.
     *
     * @param selectedSheetViewController the main controller managing the sheet view.
     */
    public SelectedSheetViewHeaderController(SelectedSheetViewController selectedSheetViewController) {
        this.selectedSheetViewController = selectedSheetViewController;
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up listeners for user interactions such as changing colors, text alignment, and spinner values.
     */
    @FXML
    private void initialize() {
        // Listens to background color changes
        cellBackgroundColorPicker.setOnAction(event -> {
            Color backgroundColor = cellBackgroundColorPicker.getValue();
            selectedSheetViewController.setSelectedCellBackgroundColor(backgroundColor);
        });

        // Listens to text color changes
        textColorPicker.setOnAction(event -> {
            Color textColor = textColorPicker.getValue();
            selectedSheetViewController.setSelectedCellTextColor(textColor);
        });

        // Listens to column width spinner changes
        columnWidthListener = (obs, oldVal, newVal) -> {
            if (selectedSheetViewController != null) {
                selectedSheetViewController.setSelectedColumnWidth(newVal);
            }
        };
        columnWidthSpinner.valueProperty().addListener(columnWidthListener);

        // Listens to row height spinner changes
        rowHeightListener = (obs, oldVal, newVal) -> {
            if (selectedSheetViewController != null) {
                selectedSheetViewController.setSelectedRowHeight(newVal);
            }
        };
        rowHeightSpinner.valueProperty().addListener(rowHeightListener);

        // Listens to alignment changes
        alignmentChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            Pos alignment = Pos.CENTER_LEFT;  // Default alignment
            switch (newVal) {
                case "Center":
                    alignment = Pos.CENTER;
                    break;
                case "Left":
                    alignment = Pos.CENTER_LEFT;
                    break;
                case "Right":
                    alignment = Pos.CENTER_RIGHT;
                    break;
            }
            selectedSheetViewController.setSelectedColumnAlignment(alignment);  // Apply selected alignment
        });

        // Set options for alignment choice box
        ObservableList<String> options = FXCollections.observableArrayList("Left", "Center", "Right");
        alignmentChoiceBox.setItems(options);

        // Initialize spinner value factories for column width and row height
        SpinnerValueFactory<Integer> widthValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 1);
        columnWidthSpinner.setValueFactory(widthValueFactory);

        SpinnerValueFactory<Integer> rowValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 1);
        rowHeightSpinner.setValueFactory(rowValueFactory);

        // Re-enable listeners after initialization
        columnWidthSpinner.valueProperty().addListener(columnWidthListener);
        rowHeightSpinner.valueProperty().addListener(rowHeightListener);
    }

    public void setSelectedSheetViewController(SelectedSheetViewController selectedSheetViewController) {
        this.selectedSheetViewController = selectedSheetViewController;
    }

    // Event handler methods for buttons (to be implemented)
    @FXML
    void ClickMeBackToDefaultButton(ActionEvent event) {}

    @FXML
    void ClickMeUpdateValueButtonAction(ActionEvent event) {}

    @FXML
    void ClickMeVersionSelectorButtonAction(ActionEvent event) {}

    @FXML
    void changeToFirstStyle(ActionEvent event) {}

    @FXML
    void changeToSecondStyle(ActionEvent event) {}

    @FXML
    void handleBackToDashboard(ActionEvent event) {
        selectedSheetViewController.backToDashboard();
    }

    /**
     * Binds the UI components to the selection state of a cell.
     * Disables or enables components based on whether a cell is selected.
     *
     * @param isSelected the BooleanProperty indicating whether a cell is selected.
     */
    public void bindingToIsSelected(BooleanProperty isSelected) {
        columnWidthSpinner.disableProperty().bind(isSelected.not());
        rowHeightSpinner.disableProperty().bind(isSelected.not());
        actionLineTextField.disableProperty().bind(isSelected.not());
        alignmentChoiceBox.disableProperty().bind(isSelected.not());
        cellBackgroundColorPicker.disableProperty().bind(isSelected.not());
        textColorPicker.disableProperty().bind(isSelected.not());
        BackToDefaultButton.disableProperty().bind(isSelected.not());
        updateValueButton.disableProperty().bind(isSelected.not());
    }


}

