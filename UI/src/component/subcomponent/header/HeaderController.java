package component.subcomponent.header;

import component.main.app.AppController;
import component.subcomponent.popup.versionselector.VersionSelectorController;
import jakarta.xml.bind.JAXBException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jaxb.schema.xmlprocessing.FileDataException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HeaderController {


        private AppController appController;

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
        @FXML private Label rowWidthLabel;
        @FXML private Spinner<Double> doubleSpinner;
        @FXML private Label columnWidthLabel;
        @FXML private Spinner<Double> columnWidthSpinner;



        public void setAppController(AppController appController) {
            this.appController = appController;
        }

        @FXML
        private void initialize() {
            // alignment box
            ObservableList<String> options =
                    FXCollections.observableArrayList( "Left", "Center", "Right" );
            alignmentChoiceBox.setItems(options);

            setDisableInButton(true);

        }


        // only yarden
        @FXML
        public void CliclMeLoadFileButtonAction() {

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

                } catch (IllegalArgumentException e) {
                    // Handle errors from isXmlFile (e.g., invalid file path or non-XML file)
                    String message = e.getMessage();
                    updateFilePathLabel(message);

                } catch (FileDataException.InvalidRowCountException e) {
                    // Print a specific error message for invalid row count
                    String message = "The XML file specifies an " + e.getMessage() + ".\nPlease ensure the sheet has between 1 and 50 rows.";
                    updateFilePathLabel(message);

                } catch (FileDataException.InvalidColumnCountException e) {
                    // Print a specific error message for invalid column count
                    String message = "The XML file specifies an " + e.getMessage() + ".\nPlease ensure the sheet has between 1 and 20 columns.";
                    updateFilePathLabel(message);

                } catch (FileDataException.InvalidColumnWidthException e) {
                    // Print a specific error message for invalid column width
                    String message = "The XML file specifies an " + e.getMessage() + ".\nPlease ensure the column width is a positive number.";
                    updateFilePathLabel(message);

                } catch (FileDataException.InvalidRowHeightException e) {
                    // Print a specific error message for invalid row height
                    String message = "The XML file specifies an " + e.getMessage() + ".\nPlease ensure the row width is a positive number.";
                    updateFilePathLabel(message);


                } catch (FileDataException.CellOutOfBoundsException e) {
                    // Print a specific error message for cell out of bounds
                    String message = "The file contains one or more cells that are positioned outside the valid sheet boundaries. \nPlease ensure all cells in the file are within the defined grid of the sheet.";
                    updateFilePathLabel(message);

                } catch (FileDataException.CircularReferenceException e) {
                    // Print a specific error message for circular reference
                    String message = "The file contains a circular reference, which is not allowed.";
                    updateFilePathLabel(message);


                } catch (FileNotFoundException e) {
                    // Handle file not found
                    String message = "The file was not found at the specified path: '" + selectedFile.getAbsolutePath() + "'.";
                    updateFilePathLabel(message);

                } catch (JAXBException e) {
                    String message = e.getMessage();
                    updateFilePathLabel(message);

                } catch (Exception e) {
                    // Catch any other exceptions
                    String message = "An unexpected error occurred: " + e.getMessage();
                    updateFilePathLabel(message);
                }
            } else {
                filePathLlabel.setText("No file selected");
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
        void CliclMeVersionSelectorButtomAction(ActionEvent event) {

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

        public void updateLabels(String cellId, String originalValue, String lastModifiedVersion) {
            cellIdLabel.setText(cellId);
            originalValueLabel.setText(originalValue);
            lastModifiedVersionLabel.setText(lastModifiedVersion);
        }

        private void setDisableInButton(boolean isEnable){
            updateValueButton.setDisable(isEnable);
            versionSelectorButtom.setDisable(isEnable);
        }

}
