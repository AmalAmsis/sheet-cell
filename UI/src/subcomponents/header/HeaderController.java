package subcomponents.header;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HeaderController {

    @FXML
    private Label cellIdLabel;

    @FXML
    private Label currentVersionLabel;

    @FXML
    private Label filePathLlabel;

    @FXML
    private Button loadFileButton;

    @FXML
    private Label originalValueLabel;

    @FXML
    private Button updateValueButtom;

    @FXML
    private Button versionSelectorButtom;

    private SimpleStringProperty selectedFileProperty;
    private Stage primaryStage;


    public void initialize() {
        // בתחילת הדרך נוודא שכל הכפתורים חוץ מ-loadFileButton יהיו מושבתים
        updateValueButtom.setDisable(true);
        versionSelectorButtom.setDisable(true);
        //actionTextField.setDisable(true);
        cellIdLabel.setDisable(true);
        originalValueLabel.setDisable(true);
        currentVersionLabel.setDisable(true);
    }



    @FXML
    public void CliclMeLoadFileButtonAction() {
        // יצירת FileChooser

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        // הוספת פילטרים לקבצים ספציפיים (אופציונלי)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );

        // פתיחת דיאלוג בחירת קובץ
        Stage stage = (Stage) loadFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // אם נבחר קובץ, הצגת הנתיב בתווית
        if (selectedFile != null) {
            filePathLlabel.setText(selectedFile.getAbsolutePath());


            // אם הקובץ נטען בהצלחה, נשחרר את הכפתורים וה-TextField
            updateValueButtom.setDisable(false);
            versionSelectorButtom.setDisable(false);
            //actionTextField.setDisable(false);
            cellIdLabel.setDisable(false);
            originalValueLabel.setDisable(false);
            currentVersionLabel.setDisable(false);
        } else {
            filePathLlabel.setText("No file selected");
        }
    }


}
