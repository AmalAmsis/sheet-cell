package component.selectedSheetView.subcomponent.left;

import JsonSerializer.JsonSerializer;
import component.popup.error.ErrorMessage;
import component.popup.viewonlysheet.ViewOnlySheetController;
import component.selectedSheetView.main.SelectedSheetViewController;
import dto.DTOSheet;
import javafx.application.Platform;
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
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.Constants.SORT_SHEET;

/**
 * Controller for the left panel of the selected sheet view.
 * This panel includes functionality for managing ranges, sorting, and filtering.
 * It provides the user with options to add or remove ranges, as well as configure sorting and filtering preferences.
 */
public class SelectedSheetViewLeftController {

    private SelectedSheetViewController selectedSheetViewController;

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

    @FXML private Button generateGraphButton;

    /**
     * Sets the main controller for this component.
     *
     * @param selectedSheetViewController The main controller of the selected sheet view.
     */
    public void setSelectedSheetViewController(SelectedSheetViewController selectedSheetViewController) {
        this.selectedSheetViewController = selectedSheetViewController;
    }

    /**
     * Initializes the controller.
     * Sets up listeners and disables buttons by default until the user selects options.
     */
    public void initialize() {
        // Disable buttons initially
        removeRangeButton.setDisable(true);
        showRangeButton.setDisable(true);
        addNewRangeButton.setDisable(true);

        // Listener for changes in the remove range choice box
        removeRangeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeRangeButton.setDisable(newValue == null);  // Enable button only if an option is selected
        });

        // Listener for changes in the show range choice box
        showRangeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showRangeButton.setDisable(newValue == null);  // Enable button only if an option is selected
        });

        // Listeners for text fields
        newRangeNameTextField.textProperty().addListener((observable, oldValue, newValue) -> validateAddNewRangeButton());
        fromTextField.textProperty().addListener((observable, oldValue, newValue) -> validateAddNewRangeButton());
        toTextField.textProperty().addListener((observable, oldValue, newValue) -> validateAddNewRangeButton());

        // Set up listeners for sorting and filtering menus
        selectColumnsToSortByMenu.setOnShowing(event -> updateMenuItems(selectColumnsToSortByMenu, selectedSortColumnsFlowPane, sortFromTextField, sortToTextField));
        selectColumnsToFilterByMenu.setOnShowing(event -> updateMenuItems(selectColumnsToFilterByMenu, filterDataFlowPane, filterFromTextField, filterToTextField));
    }

    /**
     * Validates whether the Add New Range button should be enabled based on input in text fields.
     */
    private void validateAddNewRangeButton() {
        // Enable button only if all required fields are filled
        boolean disableButton = newRangeNameTextField.getText().trim().isEmpty()
                || fromTextField.getText().trim().isEmpty()
                || toTextField.getText().trim().isEmpty();
        addNewRangeButton.setDisable(disableButton);
    }

    /**
     * Binds UI components to the file load state.
     * Disables or enables fields and buttons depending on whether a file is loaded.
     *
     * @param isFileLoaded Boolean property indicating if a file is loaded.
     */
    public void bindingToIsFileLoaded(BooleanProperty isFileLoaded) {
        toTextField.disableProperty().bind(isFileLoaded.not());
        fromTextField.disableProperty().bind(isFileLoaded.not());
        filterToTextField.disableProperty().bind(isFileLoaded.not());
        filterFromTextField.disableProperty().bind(isFileLoaded.not());
        sortToTextField.disableProperty().bind(isFileLoaded.not());
        sortFromTextField.disableProperty().bind(isFileLoaded.not());
        removeRangeChoiceBox.disableProperty().bind(isFileLoaded.not());
        showRangeChoiceBox.disableProperty().bind(isFileLoaded.not());
        generateGraphButton.disableProperty().bind(isFileLoaded.not());
    }

    /**
     * Binds the numeric cell selection state to UI components.
     * Enables or disables components based on whether a numeric cell is selected.
     *
     * @param isCellSelected Boolean property indicating if a numeric cell is selected.
     */
    public void bindingToNumericCellIsSelected(BooleanProperty isCellSelected) {
        maxValueField.disableProperty().bind(isCellSelected.not());
        minValueField.disableProperty().bind(isCellSelected.not());
        stepSizeField.disableProperty().bind(isCellSelected.not());
        applyButton.disableProperty().bind(isCellSelected.not());
    }

    /**
     * Updates the available ranges in the choice boxes.
     */
    public void updateChoiceBoxes() {
        // Get the updated list of ranges from the main controller
        List<String> ranges = selectedSheetViewController.getAllRanges();

        // Update the ChoiceBoxes with the new range options
        removeRangeChoiceBox.getItems().setAll(ranges);
        showRangeChoiceBox.getItems().setAll(ranges);
    }

    @FXML void ClickMeAddNewRangeButton(ActionEvent event) {}

    @FXML void ClickMeFilterButton(ActionEvent event) {}

    @FXML void ClickMeRemoveRange(ActionEvent event) {}

    @FXML void ClickMeShowRange(ActionEvent event) {}

    @FXML void clickMeResetFilterButton(ActionEvent event) {}

    @FXML void clickMeResetSortButton(ActionEvent event) {}

    @FXML void handleDynamicAnalysis(ActionEvent event) {}

    @FXML void handleGenerateGraph(ActionEvent event) {}

    @FXML
    void ClickMeSortButton(ActionEvent event) {

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

            // Convert the list of column priorities to a string
            String prioritiesListStr = listOfColumnsPriorities.stream()
                    .map(String::valueOf)
                    .reduce("", (acc, item) -> acc + item);

            // Build the request URL with the necessary parameters
            String url = SORT_SHEET +
                    "?sheetName=" + selectedSheetViewController.getFileName() +
                    "&from=" + from + "&to=" + to + "&prioritiesList=" + prioritiesListStr;

            // Create the request object
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

            try {
                Response response = call.execute();

                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();

                    JsonSerializer jsonSerializer = new JsonSerializer();
                    DTOSheet sortedSheet = jsonSerializer.convertJsonToDto(jsonResponse);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/popup/viewonlysheet/viewOnlySheet.fxml"));
                    Parent root = loader.load();

                    ViewOnlySheetController viewOnlySheetController = loader.getController();
                    viewOnlySheetController.setAppController(selectedSheetViewController);
                    viewOnlySheetController.initViewOnlySheetAndBindToUIModel(sortedSheet, true);

                    Stage stage = new Stage();
                    stage.setTitle("Sorted Sheet");
                    stage.setScene(new Scene(root));
                    stage.show();

                } else {
                    new ErrorMessage("Failed to fetch sheet: " + response.code());
                }

            } catch (IOException e) {
                new ErrorMessage(e.getMessage());
            }

        } catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }
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


    /**
     * Updates the menu items for sorting or filtering.
     *
     * @param menuButton The menu to update.
     * @param flowPane The flow pane that holds the selected columns.
     * @param fromTextField The starting range text field.
     * @param toTextField The ending range text field.
     */
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

    private MenuButton setupCustomMenuButtonForColumnSelection(String column,int firsRow,int lastRow) {
        // יצירת MenuButton עבור העמודה
        MenuButton menuButton = new MenuButton(column);

        // קבלת ערכים עבור העמודה
        List<String> columnsValues = selectedSheetViewController.getColumnValues(column.charAt(0),firsRow,lastRow);

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

}
