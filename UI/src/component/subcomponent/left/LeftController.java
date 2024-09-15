package component.subcomponent.left;

import component.main.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.List;


public class LeftController {

    private AppController appController;

    @FXML private TextField newRangeNameTextField;
    @FXML private TextField fromTextField;
    @FXML private TextField toTextField;
    @FXML private Button addNewRangeButton;
    @FXML private Button removeRangeButton;
    @FXML private ChoiceBox<String> removeRangeChoiceBox;
    @FXML private Button showRangeButton;
    @FXML private ChoiceBox<String> showRangeChoiceBox;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void initialize() {

        // יש לוודא שהכפתורים מושבתים כברירת מחדל בהתחלה, עד שנבחרת אפשרות
        removeRangeButton.setDisable(true);
        showRangeButton.setDisable(true);
        addNewRangeButton.setDisable(true);

        // מאזין לשינויים בתיבת הבחירה של מחיקת טווחים
        removeRangeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeRangeButton.setDisable(newValue == null);  // מאפשר לחיצה רק אם נבחרה אופציה
        });

        // מאזין לשינויים בתיבת הבחירה של הצגת טווחים
        showRangeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showRangeButton.setDisable(newValue == null);  // מאפשר לחיצה רק אם נבחרה אופציה
        });

        // מאזינים לשינויים בטקסט בשדות TextField
        newRangeNameTextField.textProperty().addListener((observable, oldValue, newValue) -> validateAddNewRangeButton());
        fromTextField.textProperty().addListener((observable, oldValue, newValue) -> validateAddNewRangeButton());
        toTextField.textProperty().addListener((observable, oldValue, newValue) -> validateAddNewRangeButton());


    }

    // פונקציה לבדיקת השדות ולהפעלת הכפתור
    private void validateAddNewRangeButton() {
        // בדיקה אם כל השדות אינם ריקים
        boolean disableButton = newRangeNameTextField.getText().trim().isEmpty()
                || fromTextField.getText().trim().isEmpty()
                || toTextField.getText().trim().isEmpty();

        // מאפשר לחיצה רק אם כל השדות מלאים
        addNewRangeButton.setDisable(disableButton);
    }

    @FXML
    void ClickMeAddNewRangeButton(ActionEvent event) {
        String rangeName = newRangeNameTextField.getText();
        String from = fromTextField.getText();
        String to = toTextField.getText();

        if (rangeName.isEmpty() || from.isEmpty() || to.isEmpty()) {
            // Show an error message or prompt the user to fill in all fields
            return;
        }


        // Call appController to add the new range
        appController.addNewRange(rangeName, from, to);

        // Clear text fields after successful operation
        newRangeNameTextField.clear();
        fromTextField.clear();
        toTextField.clear();

        // Optionally update the choice boxes
        updateChoiceBoxes();

    }

    @FXML
    void ClickMeRemoveRange(ActionEvent event) {
        String selectedRange = removeRangeChoiceBox.getSelectionModel().getSelectedItem();

        if (selectedRange != null) {
            // Call appController to remove the selected range
            appController.removeRange(selectedRange);
            removeRangeChoiceBox.getSelectionModel().clearSelection();
            // Update the choice boxes after removal
            updateChoiceBoxes();
        }

    }

    @FXML
    void ClickMeShowRange(ActionEvent event) {
        String selectedRange = showRangeChoiceBox.getSelectionModel().getSelectedItem();

        if (selectedRange != null) {
            // Call appController to show/highlight the selected range
            appController.showRange(selectedRange);
            showRangeChoiceBox.getSelectionModel().getSelectedItem();
        }
    }

    public void updateChoiceBoxes() {
        // Get the updated list of ranges from appController
        List<String> ranges = appController.getAllRanges();

        // Update the ChoiceBoxes
        removeRangeChoiceBox.getItems().setAll(ranges);
        showRangeChoiceBox.getItems().setAll(ranges);
    }

}
