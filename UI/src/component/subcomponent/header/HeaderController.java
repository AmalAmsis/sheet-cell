package component.subcomponent.header;

import component.main.app.AppController;
import component.subcomponent.popup.errormessage.ErrorMessage;
import component.subcomponent.popup.versionselector.VersionSelectorController;
import component.subcomponent.sheet.CellStyle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    @FXML private Label currentVersionLabel; // only Yarden
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

    /**
     * Sets the reference to the main application controller.
     * @param appController the main application controller
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

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

        // Set options for alignment choice box
        ObservableList<String> options =
                FXCollections.observableArrayList("Left", "Center", "Right");
        alignmentChoiceBox.setItems(options);

        // Temporarily disable listeners
        columnWidthSpinner.valueProperty().removeListener(columnWidthListener);
        rowHeightSpinner.valueProperty().removeListener(rowHeightListener);

        // Initialize spinner value factories
        SpinnerValueFactory<Integer> widthValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 1);
        columnWidthSpinner.setValueFactory(widthValueFactory);

        SpinnerValueFactory<Integer> rowValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 1);
        rowHeightSpinner.setValueFactory(rowValueFactory);

        // Re-enable listeners
        columnWidthSpinner.valueProperty().addListener(columnWidthListener);
        rowHeightSpinner.valueProperty().addListener(rowHeightListener);

        // Disable certain buttons initially
        // setDisableInButton(true);
    }

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
        // Create the loading task
        Task<Boolean> loadFileTask = new LoadFileTask(
                filePath,
                (path) -> {
                    try {
                        appController.loadAndDisplaySheetFromXmlFile(path);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                },
                (path) -> {
                    updateFilePathLabel(path); // Update file path label

                }
        );

        // Bind progress bar to task's progress
        fileLoadingProgressBar.progressProperty().bind(loadFileTask.progressProperty());
        fileLoadingProgressBar.setVisible(true);  // Show the progress bar when the task starts

        // Update status label during the task
        loadFileTask.messageProperty().addListener((obs, oldMessage, newMessage) -> {
            progressLabel.setText(newMessage);  // Update label with the current task status
        });

        // Handle task completion
        loadFileTask.setOnSucceeded(event -> {
            fileLoadingProgressBar.setVisible(false);  // Hide the progress bar upon successful completion
            progressLabel.setVisible(false);  // Hide the status label
        });

        // Handle task failure
        loadFileTask.setOnFailed(event -> {
            new ErrorMessage("Failed to load the file.");
            fileLoadingProgressBar.setVisible(false);  // Hide the progress bar if the task fails
        });

        // Start the task in a new thread
        Thread thread = new Thread(loadFileTask);
        thread.setDaemon(true);  // Ensure the thread doesn't block JVM shutdown
        thread.start();
    }

    /**
     * Click event for loading a file with progress updates.
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
     * Click event to update the selected cell value.
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
     * Updates header values including text color, background color, alignment, etc.
     * @param cellId               the ID of the cell
     * @param originalValue        the original value of the cell
     * @param lastModifiedVersion  the last modified version
     * @param textColor            the text color
     * @param backgroundColor      the background color
     * @param width                the column width
     * @param height               the row height
     * @param alignment            the alignment of the cell
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

//    /**
//     * Enables or disables certain buttons based on user interaction.
//     * @param isEnable true to disable buttons, false to enable
//     */
//    private void setDisableInButton(boolean isEnable) {
//        updateValueButton.setDisable(isEnable);
//        versionSelectorButton.setDisable(isEnable);
//    }

    /**
     * Resets the cell's text color and background color to default values.
     */
    @FXML
    void ClickMeBackToDefaultButton(ActionEvent event) {
        appController.setSelectedCellBackgroundColor(CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue());
        appController.setSelectedCellTextColor(CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue());
        cellBackgroundColorPicker.setValue(CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue());
        textColorPicker.setValue(CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue());
    }
}
