package component.subcomponent.left;

import component.main.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;


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

    }

    @FXML
    void ClickMeRemoveRange(ActionEvent event) {

    }

    @FXML
    void ClickMeShowRange(ActionEvent event) {

    }

}
