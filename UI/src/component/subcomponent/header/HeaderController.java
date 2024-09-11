package component.subcomponent.header;

import component.main.app.AppController;
import jakarta.xml.bind.JAXBException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jaxb.schema.xmlprocessing.FileDataException;

import java.io.File;
import java.io.FileNotFoundException;

public class HeaderController {

        private AppController appController;

        @FXML private Label cellIdLabel;
        @FXML private Label currentVersionLabel;
        @FXML private Label filePathLlabel;
        @FXML private Button loadFileButton;
        @FXML private Label originalValueLabel;
        @FXML private Button updateValueButtom;
        @FXML private Button versionSelectorButtom;


        public void setAppController(AppController appController) {
            this.appController = appController;
        }

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


        @FXML void CliclMeUpdateValueButtonAction(ActionEvent event) {

        }

        @FXML void CliclMeVersionSelectorButtomAction(ActionEvent event) {

        }

}
