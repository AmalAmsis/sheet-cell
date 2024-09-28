package component.subcomponent.header;

import component.main.app.AppController;
import component.subcomponent.popup.errormessage.ErrorMessage;
import component.subcomponent.popup.versionselector.VersionSelectorController;
import component.subcomponent.sheet.CellStyle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import task.LoadFileTask;

import java.io.File;
import java.io.IOException;

public class HeaderController {

    private AppController appController; // Reference to the main application controller

    // Global listeners for column width and row height changes
    private ChangeListener<Integer> columnWidthListener;
    private ChangeListener<Integer> rowHeightListener;

    @FXML private Label cellIdLabel;
    @FXML private Label currentVersionLabel;
    @FXML private Label filePathLlabel;
    @FXML private Button loadFileButton;
    @FXML private Label originalValueLabel;
    @FXML private Button getUpdateValueButton;
    @FXML private TextField actionLineTextField;
    @FXML private Button updateValueButton;
    @FXML private Button versionSelectorButton;
    @FXML private Label lastModifiedVersionLabel;
    @FXML private ColorPicker cellBackgroundColorPicker;
    @FXML private ColorPicker textColorPicker;
    @FXML private Label alignmentLabel;
    @FXML private ChoiceBox<String> alignmentChoiceBox;
    @FXML private Label rowHeightLabel;
    @FXML private Spinner<Integer> rowHeightSpinner;
    @FXML private Label columnWidthLabel;
    @FXML private Spinner<Integer> columnWidthSpinner;
    @FXML private Button BackToDefaultButton;
    @FXML private ProgressBar fileLoadingProgressBar;  // Progress bar for loading
    @FXML private Label progressLabel;  // Label for status updates
    @FXML private MenuButton themesMenuButton;

    /**
     * Sets the reference to the main application controller.
     * @param appController the main application controller
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    /**
     * Initializes listeners and binds controls to their respective properties.
     */
    @FXML
    private void initialize() {
        // Listens to background color changes
        cellBackgroundColorPicker.setOnAction(event -> {
            Color backgroundColor = cellBackgroundColorPicker.getValue();
            appController.setSelectedCellBackgroundColor(backgroundColor);
        });

        // Listens to text color changes
        textColorPicker.setOnAction(event -> {
            Color textColor = textColorPicker.getValue();
            appController.setSelectedCellTextColor(textColor);
        });

        // Listens to column width spinner changes
        columnWidthListener = (obs, oldVal, newVal) -> {
            if (appController != null) {
                appController.setSelectedColumnWidth(newVal);
            }
        };
        columnWidthSpinner.valueProperty().addListener(columnWidthListener);

        // Listens to row height spinner changes
        rowHeightListener = (obs, oldVal, newVal) -> {
            if (appController != null) {
                appController.setSelectedRowHeight(newVal);
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
            appController.setSelectedColumnAlignment(alignment);  // Apply selected alignment

        });

        themesMenuButton.setText("Style 1");

        // Set options for alignment choice box
        ObservableList<String> options = FXCollections.observableArrayList("Left", "Center", "Right");
        alignmentChoiceBox.setItems(options);

        // Temporarily disable listeners
        columnWidthSpinner.valueProperty().removeListener(columnWidthListener);
        rowHeightSpinner.valueProperty().removeListener(rowHeightListener);

        // Initialize spinner value factories
        SpinnerValueFactory<Integer> widthValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 1);
        columnWidthSpinner.setValueFactory(widthValueFactory);

        SpinnerValueFactory<Integer> rowValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 1);
        rowHeightSpinner.setValueFactory(rowValueFactory);

        // Re-enable listeners
        columnWidthSpinner.valueProperty().addListener(columnWidthListener);
        rowHeightSpinner.valueProperty().addListener(rowHeightListener);

        // Disable certain buttons initially
        // setDisableInButton(true);
    }

    /**
     * Binds the visibility and activity of various controls to whether a cell is selected.
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

    /**
     * Binds the visibility and activity of controls based on whether a file is loaded.
     */
    public void bindingToIsFileLoaded(BooleanProperty isFileLoaded) {
        versionSelectorButton.disableProperty().bind(isFileLoaded.not());
    }

    /**
     * Updates the file path label with the given message.
     * @param message the new file path
     */
    public void updateFilePathLabel(String message) {
        filePathLlabel.setText(message);
    }

    /**
     * Loads the selected file with a progress bar, showing updates to the user.
     * @param filePath the file path to be loaded
     */
    private void loadFileWithProgress(String filePath) {
        Task<Boolean> loadFileTask = new LoadFileTask(
                filePath,
                (path) -> {
                    try {
                        appController.loadAndDisplaySheetFromXmlFile(path);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                },
                (path) -> updateFilePathLabel(path)
        );

        fileLoadingProgressBar.progressProperty().bind(loadFileTask.progressProperty());
        fileLoadingProgressBar.setVisible(true);

        loadFileTask.messageProperty().addListener((obs, oldMessage, newMessage) -> {
            progressLabel.setText(newMessage);
        });

        loadFileTask.setOnSucceeded(event -> {
            fileLoadingProgressBar.setVisible(false);
            progressLabel.setVisible(false);
        });

        loadFileTask.setOnFailed(event -> {
            new ErrorMessage("Failed to load the file.");
            fileLoadingProgressBar.setVisible(false);
        });

        Thread thread = new Thread(loadFileTask);
        thread.setDaemon(true);  // Ensure the thread doesn't block JVM shutdown
        thread.start();
    }

    /**
     * Handles file selection and loading.
     */
    @FXML
    public void ClickMeLoadFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        Stage stage = (Stage) loadFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            loadFileWithProgress(selectedFile.getAbsolutePath());
        } else {
            new ErrorMessage("No file selected");
        }
    }

    /**
     * Updates the selected cell value.
     */
    @FXML
    void ClickMeUpdateValueButtonAction(ActionEvent event) {
        String originalValue = actionLineTextField.getText();
        appController.updateCellValue(originalValue);
        actionLineTextField.clear();
    }

    /**
     * Opens the version selector popup for selecting a version.
     */
    @FXML
    void ClickMeVersionSelectorButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/subcomponent/popup/versionselector/versionSelector.fxml"));
            Parent root = loader.load();

            VersionSelectorController controller = loader.getController();
            controller.setAppController(appController);
            controller.loadVersionToMenuBar();

            Stage stage = new Stage();
            stage.setTitle("Version Selector");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * Resets the cell's text and background color to default values.
     */
    @FXML
    void ClickMeBackToDefaultButton(ActionEvent event) {
        appController.setSelectedCellBackgroundColor(CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue());
        appController.setSelectedCellTextColor(CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue());
        cellBackgroundColorPicker.setValue(CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue());
        textColorPicker.setValue(CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue());
    }

    /**
     * Changes the theme to the first style.
     */
    public void changeToFirstStyle(ActionEvent actionEvent) {
        appController.applyTheme("Style 1");
        themesMenuButton.setText("Style 1");
    }

    /**
     * Changes the theme to the second style.
     */
    public void changeToSecondStyle(ActionEvent actionEvent) {
        appController.applyTheme("Style 2");
        themesMenuButton.setText("Style 2");
    }
}
