package component.selectedSheetView.subcomponent.header;

import component.popup.versionselector.VersionSelectorController;
import component.selectedSheetView.main.SelectedSheetViewController;
import component.selectedSheetView.subcomponent.sheet.CellStyle;
import dto.DTOSheet;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

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
    @FXML private Button latestVersionButton;



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
    /**
     * Resets the cell's text and background color to default values.
     */
    @FXML
    void ClickMeBackToDefaultButton(ActionEvent event) {
        selectedSheetViewController.setSelectedCellBackgroundColor(CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue());
        selectedSheetViewController.setSelectedCellTextColor(CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue());
        cellBackgroundColorPicker.setValue(CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue());
        textColorPicker.setValue(CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue());
    }

    @FXML
    void ClickMeUpdateValueButtonAction(ActionEvent event) {
        String originalValue = actionLineTextField.getText();
        selectedSheetViewController.updateCellValue(originalValue);
        actionLineTextField.clear();
    }

    @FXML
    void ClickMeVersionSelectorButtonAction(ActionEvent event) {

        Platform.runLater(() ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/popup/versionselector/versionSelector.fxml"));
                Parent root = loader.load();

                VersionSelectorController versionSelectorController = loader.getController();
                versionSelectorController.setSelectedSheetViewController(selectedSheetViewController);
                versionSelectorController.loadVersionToMenuBar();

                Stage stage = new Stage();
                stage.setTitle("Version Selector");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Changes the theme to the first style.
     */
    @FXML
    public void changeToFirstStyle(ActionEvent actionEvent) {
        selectedSheetViewController.applyTheme("Style 1");
        themesMenuButton.setText("Style 1");
    }

    /**
     * Changes the theme to the second style.
     */
    @FXML
    public void changeToSecondStyle(ActionEvent actionEvent) {
        selectedSheetViewController.applyTheme("Style 2");
        themesMenuButton.setText("Style 2");
    }

    @FXML
    void handleBackToDashboard(ActionEvent event) {
        selectedSheetViewController.backToDashboard();
    }

    @FXML
    void handleSwitchToLatestVersion(ActionEvent event) {
        String currentSheetName = selectedSheetViewController.getCurrentSheetName();
        DTOSheet dtoSheet = selectedSheetViewController.getDtoSheet(currentSheetName);
        selectedSheetViewController.updateSheet(dtoSheet);
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


    public Button getSwitchToTheLatestVersionButton() {
        return latestVersionButton;
    }

    /**
     * Updates header values, including text color, background color, alignment, etc.
     */
    public void updateHeaderValues(String cellId, String originalValue, String lastModifiedVersion, Color textColor, Color backgroundColor, int width, int height, String alignment) {
        cellIdLabel.setText(cellId);
        originalValueLabel.setText(originalValue);
        lastModifiedVersionLabel.setText(lastModifiedVersion);
        cellBackgroundColorPicker.setValue(backgroundColor);
        textColorPicker.setValue(textColor);
        columnWidthSpinner.getValueFactory().setValue(width);
        rowHeightSpinner.getValueFactory().setValue(height);
        alignmentChoiceBox.getSelectionModel().select(alignment);
    }
}

