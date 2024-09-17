package component.subcomponent.header;

import component.main.app.AppController;
import component.subcomponent.popup.errormessage.ErrorMessage;
import component.subcomponent.popup.versionselector.VersionSelectorController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HeaderController {


        private AppController appController;

        // הגדרת מאזינים כמשתנים גלובליים (שדות של המחלקה)
        private ChangeListener<Integer> columnWidthListener;
        private ChangeListener<Integer> rowHeightListener;

        @FXML private Label cellIdLabel;
        @FXML private Label currentVersionLabel; //only Yarden
        @FXML private Label filePathLlabel;
        @FXML private Button loadFileButton;
        @FXML private Label originalValueLabel;
        @FXML private Button getUpdateValueButton;
        @FXML private Button versionSelectorButtom; //only Yarden
        @FXML private TextField actionLineTextField;
        @FXML private Button updateValueButton;
        @FXML private Button versionSelectorButton;
        @FXML private Label lastModifiedVersionLabel;
        @FXML private Label cellBackgroundColoeLabel;
        @FXML private ColorPicker cellBackgroundColorPicker;
        @FXML private Label textColorLabel;
        @FXML private ColorPicker textColorPicker;
        @FXML private Label alignmentLabel;
        @FXML private ChoiceBox<String> alignmentChoiceBox;
        @FXML private Label rowHeightLabel;
        @FXML private Spinner<Integer> rowHeightSpinner;
        @FXML private Label columnWidthLabel;
        @FXML private Spinner<Integer> columnWidthSpinner;



        public void setAppController(AppController appController) {
            this.appController = appController;
        }

        @FXML
        private void initialize() {

            // האזנה לבחירת צבע רקע
            cellBackgroundColorPicker.setOnAction(event -> {
                Color backgroundColor = cellBackgroundColorPicker.getValue();
                appController.setSelectedCellBackgroundColor(backgroundColor);
            });

            // האזנה לבחירת צבע טקסט
            textColorPicker.setOnAction(event -> {
                Color textColor = textColorPicker.getValue();
                appController.setSelectedCellTextColor(textColor);
            });

            // הגדרת האזנה לשינויים ב-Spinner של רוחב עמודות
            columnWidthListener = (obs, oldVal, newVal) -> {
                if (appController != null) {
                    appController.setSelectedColumnWidth(newVal);
                }
            };
            columnWidthSpinner.valueProperty().addListener(columnWidthListener);

            // הגדרת האזנה לשינויים ב-Spinner של גובה שורות
            rowHeightListener = (obs, oldVal, newVal) -> {
                if (appController != null) {
                    appController.setSelectedRowHeight(newVal);
                }
            };
            rowHeightSpinner.valueProperty().addListener(rowHeightListener);


            // הגדרת האזנה לבחירת יישור טקסט
            alignmentChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                Pos alignment = Pos.CENTER_LEFT;  // ברירת מחדל
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
                appController.setSelectedColumnAlignment(alignment);  // יישור הטקסט בעמודה שנבחרה
            });

            // alignment box
            ObservableList<String> options =
                    FXCollections.observableArrayList( "Left", "Center", "Right" );
            alignmentChoiceBox.setItems(options);

//            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 5);
//            columnWidthSpinner.setValueFactory(valueFactory);

            // השבתת מאזינים
            columnWidthSpinner.valueProperty().removeListener(columnWidthListener);
            rowHeightSpinner.valueProperty().removeListener(rowHeightListener);


            // עדכון ערכי ה-Spinner
            SpinnerValueFactory<Integer> widthValueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 1);
            columnWidthSpinner.setValueFactory(widthValueFactory);

            SpinnerValueFactory<Integer> rowValueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 1);
            rowHeightSpinner.setValueFactory(rowValueFactory);

            // החזרת מאזינים
            columnWidthSpinner.valueProperty().addListener(columnWidthListener);
            rowHeightSpinner.valueProperty().addListener(rowHeightListener);


            setDisableInButton(true);

        }


        @FXML
        public void ClickMeLoadFileButtonAction() {

            // Create a FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");

            // Adding filters to specific files (optional)
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("XML Files", "*.xml")
            );

            // Open a file selection dialog
            Stage stage = (Stage) loadFileButton.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                // If a file is selected, load it using the UIAdapter
                try {
                    appController.loadAndDisplaySheetFromXmlFile(selectedFile.getAbsolutePath());
                    updateFilePathLabel(selectedFile.getAbsolutePath());
                    setDisableInButton(false);

                } catch (Exception e) {
                    new ErrorMessage(e.getMessage());

                }
            } else {
                new ErrorMessage("No file selected");
            }
        }

        public void updateFilePathLabel(String message) {
            filePathLlabel.setText(message);
        }


        @FXML void ClickMeUpdateValueButtonAction(ActionEvent event) {

            String originalValue = actionLineTextField.getText();
            appController.updateCellValue(originalValue);
            actionLineTextField.clear();

        }


    // only Yarden
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


            }catch (IOException e){
                e.printStackTrace();

            }

        }

        public void updateHeaderValues(String cellId, String originalValue, String lastModifiedVersion, Color textColor, Color backgroundColor, int width, int height, String aligment) {
            cellIdLabel.setText(cellId);
            originalValueLabel.setText(originalValue);
            lastModifiedVersionLabel.setText(lastModifiedVersion);
            cellBackgroundColorPicker.setValue(backgroundColor);
            textColorPicker.setValue(textColor);
            columnWidthSpinner.getValueFactory().setValue(width);
            rowHeightSpinner.getValueFactory().setValue(height);
            alignmentChoiceBox.getSelectionModel().select(aligment);
//            // השבתת מאזינים
//            columnWidthSpinner.valueProperty().removeListener(columnWidthListener);
//            rowHeightSpinner.valueProperty().removeListener(rowHeightListener);

//            // עדכון ערכי ה-Spinner
//            SpinnerValueFactory<Integer> widthValueFactory =
//                    new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, width, 1);
//            columnWidthSpinner.setValueFactory(widthValueFactory);
//
//            SpinnerValueFactory<Integer> rowValueFactory =
//                    new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, height, 1);
//            rowHeightSpinner.setValueFactory(rowValueFactory);

//            // החזרת מאזינים
//            columnWidthSpinner.valueProperty().addListener(columnWidthListener);
//            rowHeightSpinner.valueProperty().addListener(rowHeightListener);

        }

        private void setDisableInButton(boolean isEnable){
            updateValueButton.setDisable(isEnable);
            versionSelectorButtom.setDisable(isEnable);
        }

}
