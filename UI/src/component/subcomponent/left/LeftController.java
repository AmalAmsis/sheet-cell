package component.subcomponent.left;

import component.main.app.AppController;
import component.subcomponent.popup.dynamicAnalysisSheet.DynamicAnalysisSheetController;
import component.subcomponent.popup.errormessage.ErrorMessage;
import component.subcomponent.popup.viewonlysheet.ViewOnlySheetController;
import dto.DTOSheet;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @FXML private MenuButton selectColumnsToSortByMenu;
    @FXML private FlowPane selectedSortColumnsFlowPane;
    @FXML private Button resetSortButton;

    @FXML private Button filterButton;
    @FXML private Button filterHelpButton;
    @FXML private TextField filterFromTextField;
    @FXML private TextField filterToTextField;
    @FXML private MenuButton selectColumnsToFilterByMenu;
    @FXML private FlowPane filterDataFlowPane;
    @FXML private Button resetFilterButton;

    @FXML private TextField minValueField;
    @FXML private TextField maxValueField;
    @FXML private TextField stepSizeField;
    @FXML private Slider valueSlider;
    @FXML private Button applyButton;




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



        setSortTooltip();
        sortButton.setDisable(true);
        filterButton.setDisable(true);
        selectColumnsToSortByMenu.setOnShowing(event -> updateMenuItems(selectColumnsToSortByMenu,selectedSortColumnsFlowPane,sortFromTextField,sortToTextField));

        setFilterTooltip();
        selectColumnsToFilterByMenu.setOnShowing(event -> updateMenuItems(selectColumnsToFilterByMenu,filterDataFlowPane,filterFromTextField,filterToTextField));
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
            appController.selectRange(selectedRange);
            showRangeChoiceBox.getSelectionModel().clearSelection();

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

    //SORT
    @FXML
    public void ClickMeSortButton(){

        String from = sortFromTextField.getText();
        String to = sortToTextField.getText();

        if (from.isEmpty() || to.isEmpty()) {
            // Show an error message or prompt the user to fill in all fields
            return;
        }

        List<Character> listOfColumnsPriorities = new ArrayList<>();
        for (Node labelNode : selectedSortColumnsFlowPane.getChildren()) {
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
        selectedSortColumnsFlowPane.getChildren().clear();
        selectColumnsToSortByMenu.getItems().clear();
        sortButton.setDisable(true);

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
             new ErrorMessage(e.getMessage());
        }

    }
    private void setSortTooltip(){
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
    private void setupSortMenuItemEvents(String column, MenuItem columnItem) {

        selectColumnsToSortByMenu.getItems().remove(columnItem);

        if (!selectedSortColumnsFlowPane.getChildren().contains(new Label(column))) {
            Label label = new Label(column);
            label.getStyleClass().add("label-with-border");
            selectedSortColumnsFlowPane.getChildren().add(label);
            sortButton.setDisable(false);
        }


    }
    @FXML public void clickMeResetSortButton(){
        sortFromTextField.clear();
        sortToTextField.clear();
        selectedSortColumnsFlowPane.getChildren().clear();
        selectColumnsToSortByMenu.getItems().clear();
        sortButton.setDisable(false);

    }

    public void bindingToIsFileLoaded(BooleanProperty isFileLoaded) {
        toTextField.disableProperty().bind(isFileLoaded.not());
        fromTextField.disableProperty().bind(isFileLoaded.not());
        filterToTextField.disableProperty().bind(isFileLoaded.not());
        filterFromTextField.disableProperty().bind(isFileLoaded.not());
        sortToTextField.disableProperty().bind(isFileLoaded.not());
        sortFromTextField.disableProperty().bind(isFileLoaded.not());
        removeRangeChoiceBox.disableProperty().bind(isFileLoaded.not());
        showRangeChoiceBox.disableProperty().bind(isFileLoaded.not());
    }

    public void bindingToNumericCellIsSelected(BooleanProperty isCellSelected) {
        maxValueField.disableProperty().bind(isCellSelected.not());
        minValueField.disableProperty().bind(isCellSelected.not());
        stepSizeField.disableProperty().bind(isCellSelected.not());
        applyButton.disableProperty().bind(isCellSelected.not());
    }

    //FILTER
    @FXML public void ClickMeFilterButton(){

        Map<String, List<String>> selectedColumnValues = new HashMap<>();

        for (Node node : filterDataFlowPane.getChildren()) {
            if (node instanceof MenuButton) {
                MenuButton menuButton = (MenuButton) node;
                String column = menuButton.getText();  // שם העמודה (למשל A, B, C...)

                // רשימה לאחסון הערכים המסומנים
                List<String> selectedValues = new ArrayList<>();

                // עובר על כל MenuItem בתוך MenuButton
                for (MenuItem item : menuButton.getItems()) {
                    if (item instanceof CustomMenuItem) {
                        CustomMenuItem customMenuItem = (CustomMenuItem) item;
                        Node content = customMenuItem.getContent();

                        // בודק אם התוכן הוא CheckBox
                        if (content instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) content;

                            // אם ה-CheckBox מסומן, הוסף את הערך שלו לרשימה
                            if (checkBox.isSelected()) {
                                selectedValues.add(checkBox.getText());
                            }
                        }
                    }
                }

                // אם יש ערכים שנבחרו, הוסף אותם למפה
                if (!selectedValues.isEmpty()) {
                    selectedColumnValues.put(column, selectedValues);
                }
            }



        }

        String from = filterFromTextField.getText();
        String to = filterToTextField.getText();

        if (from.isEmpty() || to.isEmpty()) {
            // Show an error message or prompt the user to fill in all fields
            return;
        }

        filterFromTextField.clear();
        filterToTextField.clear();
        filterDataFlowPane.getChildren().clear();
        selectColumnsToFilterByMenu.getItems().clear();
        filterButton.setDisable(true);


        try{
            DTOSheet filterSheet = appController.getfilterSheet(selectedColumnValues, from, to);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/subcomponent/popup/viewonlysheet/viewOnlySheet.fxml"));
            Parent root = loader.load();

            ViewOnlySheetController viewOnlySheetController = loader.getController();
            viewOnlySheetController.setAppController(appController);
            viewOnlySheetController.initViewOnlySheetAndBindToUIModel(filterSheet,true);

            Stage stage = new Stage();
            stage.setTitle("filterSheet");
            stage.setScene(new Scene(root));
            stage.show();



        } catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }




    }
    private void setFilterTooltip(){
        String tooltipText = "To filter the data int the sheet:\n"
                + "1. Enter the range in the 'from' and 'to' fields (e.g., A1 to C5).\n"
                + "   - Make sure the range covers valid rows and columns.\n"
                + "2. Select the columns to filter by clicking on the column headers.\n"
                + "3. For each selected column, choose the values you want to display after filtering.\\n\n"
                + "4. Click 'filter' to apply the filtering to the selected range.";

        Tooltip filterHelpTooltip = new Tooltip(tooltipText);
        filterHelpTooltip.setStyle("-fx-font-size: 14px;");
        filterHelpButton.setTooltip(filterHelpTooltip);  // הצמדת ה-Tooltip לכפתור
    }
    private void setupFilterMenuItemEvents(String column,MenuItem columnItem, int firsRow,int lastRow) {

        selectColumnsToFilterByMenu.getItems().remove(columnItem);

        boolean columnExists = filterDataFlowPane.getChildren().stream()
                .anyMatch(node -> node instanceof MenuButton && ((MenuButton) node).getText().equals(column));


        if (!columnExists) {
            // הוספת comboBox ל-flowPane
            filterDataFlowPane.getChildren().add(setupCustomMenuButtonForColumnSelection(column,firsRow,lastRow));
            filterButton.setDisable(false);
        }


    }
    @FXML public void clickMeResetFilterButton(){
        filterFromTextField.clear();
        filterToTextField.clear();
        filterDataFlowPane.getChildren().clear();
        selectColumnsToFilterByMenu.getItems().clear();
        filterButton.setDisable(true);
    }


    private MenuButton setupCustomMenuButtonForColumnSelection(String column,int firsRow,int lastRow) {
        // יצירת MenuButton עבור העמודה
        MenuButton menuButton = new MenuButton(column);

        // קבלת ערכים עבור העמודה
        List<String> columnsValues = appController.getColumnValues(column.charAt(0),firsRow,lastRow);

        // הוספת CheckBox עבור כל ערך ל-MenuButton
        for (String value : columnsValues) {
            CheckBox checkBox = new CheckBox(value);

            // יצירת CustomMenuItem שמכיל את ה-CheckBox
            CustomMenuItem menuItem = new CustomMenuItem(checkBox);
            menuItem.setHideOnClick(false); // מונע סגירה של ה-MenuButton לאחר לחיצה

            // הוספת ה-MenuItem ל-MenuButton
            menuButton.getItems().add(menuItem);
        }

        return menuButton;
    }



    //BOTh (SORT AND filter)
    private void updateMenuItems(MenuButton menuButton ,FlowPane flowPane,TextField fromTextField,TextField toTextField) {

        menuButton.getItems().clear();


        String fromText = fromTextField.getText();
        String toText = toTextField.getText();

        // בדיקה שהשדות אינם ריקים ושהקלט תקין
        if (fromText.isEmpty() || toText.isEmpty()) {
            return;
        }

        List<String> columnsInRange = getColumnsInRange(fromText, toText);
        int firstRowInRange = getFirstRowInRange(fromText);
        int lastRowInRange = getLastRowInRange(toText);

        // אם הפונקציה מחזירה null, לא לעדכן את התפריט
        if (columnsInRange == null) {
           new ErrorMessage("Invalid input! Please enter a valid range.");
            return;
        }

        // עדכון הרשימה ב-UI
        menuButton.getItems().clear();
        for (String column : columnsInRange) {
            MenuItem columnItem = new MenuItem(column);

            if(menuButton.equals(selectColumnsToSortByMenu)) {
                columnItem.setOnAction(event -> {
                    setupSortMenuItemEvents( column, columnItem);
                });
            }
            else {

                columnItem.setOnAction(event -> {
                    setupFilterMenuItemEvents(column, columnItem,firstRowInRange,lastRowInRange);
                });

            }
            menuButton.getItems().add(columnItem);
        }


    }
    private int getFirstRowInRange(String fromText) {
        int index =1;
        while (index < fromText.length() && !Character.isDigit(fromText.charAt(index))) {
            index++;
        }
        if (index < fromText.length()) {
            String numberPart = fromText.substring(index);
            return Integer.parseInt(numberPart); // המרת המחרוזת למספר שלם
        } else {
            throw new IllegalArgumentException("No numeric part found in the input string: " + fromText);
        }

    }
    private int getLastRowInRange(String ToText) {
        int index =1;
        while (index < ToText.length() && !Character.isDigit(ToText.charAt(index))) {
            index++;
        }
        if (index < ToText.length()) {
            String numberPart = ToText.substring(index);
            return Integer.parseInt(numberPart); // המרת המחרוזת למספר שלם
        } else {
            throw new IllegalArgumentException("No numeric part found in the input string: " + ToText);
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

        if( Integer.parseInt(fromRow) ==0 || Integer.parseInt(toRow) ==0){
            return null;
        }

        List<String> columnsInRange = new ArrayList<>();

        // עוברים על האותיות בין from ל-to
        for (char column = fromColumn; column <= toColumn; column++) {
            columnsInRange.add(String.valueOf(column)); // מוסיפים כל עמודה לרשימה
        }

        return columnsInRange;
    }

    @FXML
    private void handleDynamicAnalysis() {
        try {
            // קבלת הערכים מה-TextFields
            double minValue = Double.parseDouble(minValueField.getText());
            double maxValue = Double.parseDouble(maxValueField.getText());
            double stepSize = Double.parseDouble(stepSizeField.getText());

            //צריך לתקן
            String selectedCellKey = appController.getSelectedCellKey(); // לקבל את תא שנבחר
            showDynamicAnalysisPopup(selectedCellKey, minValue, maxValue, stepSize);



        } catch (NumberFormatException e) {
            new ErrorMessage("Please enter valid numeric values.");
        }
    }

    public void showDynamicAnalysisPopup(String cellKey, double minValue, double maxValue, double stepSize) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/subcomponent/popup/dynamicAnalysisSheet/dynamicAnalysisSheet.fxml"));
            Parent root = loader.load();

            DynamicAnalysisSheetController controller = loader.getController();
            controller.setAppController(appController);

            // Initialize the popup with the selected cell and range values
            controller.initDynamicAnalysisSheet(appController.getCurrentSheetDTO(), cellKey, minValue, maxValue, stepSize);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dynamic Analysis");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    }


