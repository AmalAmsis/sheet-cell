package component.subcomponent.left;

import component.main.app.AppController;
import component.subcomponent.popup.viewonlysheet.ViewOnlySheetController;
import dto.DTOSheet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.ArrayList;
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


    @FXML private Button sortButton;
    @FXML private Button sortHelpButton;
    @FXML private TextField sortFromTextField;
    @FXML private TextField sortToTextField;
    @FXML private MenuButton selectColumnsMenu;
    @FXML private FlowPane selectedColumnsFlowPane;


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


        //yarden
        setTooltip();
        //sortHelpButton.setDisable(true); (need to add it after)
        selectColumnsMenu.setOnShowing(event -> updateMenuItems());

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
        }
    }

    public void updateChoiceBoxes() {
        // Get the updated list of ranges from appController
        List<String> ranges = appController.getAllRanges();

        // Update the ChoiceBoxes
        removeRangeChoiceBox.getItems().setAll(ranges);
        showRangeChoiceBox.getItems().setAll(ranges);
    }




    //command section

    @FXML
    public void ClickMeSortButtom(){

        String from = sortFromTextField.getText();
        String to = sortToTextField.getText();

        if (from.isEmpty() || to.isEmpty()) {
            // Show an error message or prompt the user to fill in all fields
            return;
        }

        List<Character> listOfColumnsPriorities = new ArrayList<>();
        for (Node labelNode : selectedColumnsFlowPane.getChildren()) {
            if (labelNode instanceof Label) {
                String labelText = ((Label) labelNode).getText();
                if (!labelText.isEmpty()) {
                    // מוסיף את האות מה-LABEL לרשימה
                    listOfColumnsPriorities.add(labelText.charAt(0));
                }
            }
        }

        sortFromTextField.clear();
        sortToTextField.clear();
        selectedColumnsFlowPane.getChildren().clear();
        selectColumnsMenu.getItems().clear();

        try{
            DTOSheet sortedSheet = appController.getSortedSheet(from, to, listOfColumnsPriorities);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/subcomponent/popup/viewonlysheet/viewOnlySheet.fxml"));
            Parent root = loader.load();

            ViewOnlySheetController viewOnlySheetController = loader.getController();
            viewOnlySheetController.setAppController(appController);
            viewOnlySheetController.initViewOnlySheetAndBindToUIModel(sortedSheet,true);

            Stage stage = new Stage();
            stage.setTitle("Sorted Sheet");
            stage.setScene(new Scene(root));
            stage.show();



        } catch (Exception e) {
            System.out.println("Error during sorting: " + e.getMessage());
        }

    }


    private void setTooltip(){
        String tooltipText = "To sort the data int the sheet:\n"
                + "1. Enter the range in the 'from' and 'to' fields (e.g., A1 to C5).\n"
                + "   - Make sure the range covers valid rows and columns.\n"
                + "2. Select the columns to sort by clicking on the column headers.\n"
                + "   - The order in which you click the columns will determine the sorting priority.\n"
                + "   - The first column clicked has the highest priority, the second column clicked will be sorted within the first, and so on.\n"
                + "3. Sorting applies only to numeric values.\n"
                + "   - Cells with empty values are treated as infinity and sorted last.\n"
                + "4. Click 'Sort' to apply the sorting to the selected range.";


        Tooltip sortHelpTooltip = new Tooltip(tooltipText);
        sortHelpTooltip.setStyle("-fx-font-size: 14px;");
        sortHelpButton.setTooltip(sortHelpTooltip);  // הצמדת ה-Tooltip לכפתור
    }

    private void updateMenuItems() {

        selectColumnsMenu.getItems().clear();

        String fromText = sortFromTextField.getText();
        String toText = sortToTextField.getText();

        // בדיקה שהשדות אינם ריקים ושהקלט תקין
        if (fromText.isEmpty() || toText.isEmpty()) {
            return;
        }

        List<String> columnsInRange = getColumnsInRange(fromText, toText);

        // אם הפונקציה מחזירה null, לא לעדכן את התפריט
        if (columnsInRange == null) {
            System.out.println("Invalid input! Please enter a valid range.");
            return;
        }

        // עדכון הרשימה ב-UI
        selectColumnsMenu.getItems().clear();
        for (String column : columnsInRange) {
            MenuItem columnItem = new MenuItem(column);
            columnItem.setOnAction(event -> {setupMenuItemEvents(column, columnItem );});
            selectColumnsMenu.getItems().add(columnItem);
        }


    }

    private void setupMenuItemEvents(String column,MenuItem columnItem) {

        selectColumnsMenu.getItems().remove(columnItem);

        if (!selectedColumnsFlowPane.getChildren().contains(new Label(column))) {
            Label label = new Label(column);
            label.getStyleClass().add("label-with-border");
            selectedColumnsFlowPane.getChildren().add(label);
        }

    }


    public List<String> getColumnsInRange(String from, String to) {

        if (from.length() > 3 || to.length() > 3) {
            return null;
        }

        // להוציא את האותיות מהמחרוזות
        char fromColumn = from.toUpperCase().charAt(0); // לדוגמה A1 -> A
        char toColumn = to.toUpperCase().charAt(0); // לדוגמה C5 -> C

        // בדיקה שהתווים הבאים הם מספרים
        String fromRow = from.substring(1);
        String toRow = to.substring(1);

        try {
            Integer.parseInt(fromRow);
            Integer.parseInt(toRow);
        } catch (NumberFormatException e) {
            return null; // אם אין מספרים אחרי האותיות, מחזירים null
        }

        List<String> columnsInRange = new ArrayList<>();

        // עוברים על האותיות בין from ל-to
        for (char column = fromColumn; column <= toColumn; column++) {
            columnsInRange.add(String.valueOf(column)); // מוסיפים כל עמודה לרשימה
        }

        return columnsInRange;
    }

}
