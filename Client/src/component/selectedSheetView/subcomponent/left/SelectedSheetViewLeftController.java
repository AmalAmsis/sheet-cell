package component.selectedSheetView.subcomponent.left;

import JsonSerializer.JsonSerializer;
import com.google.gson.Gson;
import component.popup.dynamicAnalysisSheet.DynamicAnalysisSheetController;
import component.popup.error.ErrorMessage;
import component.popup.viewonlysheet.ViewOnlySheetController;
import component.selectedSheetView.main.SelectedSheetViewController;
import component.popup.graph.GraphController;
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
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import util.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

import static util.Constants.FILTER_SHEET;
import static util.Constants.SORT_SHEET;

/**
 * Controller for the left panel of the selected sheet view.
 * This panel includes functionality for managing ranges, sorting, and filtering.
 * It provides the user with options to add or remove ranges, as well as configure sorting and filtering preferences.
 */
public class SelectedSheetViewLeftController implements Closeable {

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

    Timer rangesPollimgTimer;

    /**Sets the main controller for this component.
     * @param selectedSheetViewController The main controller of the selected sheet view.*/
    public void setSelectedSheetViewController(SelectedSheetViewController selectedSheetViewController) {
        this.selectedSheetViewController = selectedSheetViewController;
    }

    /**Initializes the controller.Sets up listeners and disables buttons by default until the user selects options.*/
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
       // updateChoiceBoxes();
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

        List<String> ranges = selectedSheetViewController.getAllRanges();
        removeRangeChoiceBox.getItems().setAll(ranges);
        showRangeChoiceBox.getItems().setAll(ranges);

    }

    public void updateChoiceBoxesWithNewRanges(List<String> newRanges) {
        removeRangeChoiceBox.getItems().setAll(newRanges);
        showRangeChoiceBox.getItems().setAll(newRanges);
    }



    @FXML void ClickMeAddNewRangeButton(ActionEvent event) {
        String rangeName = newRangeNameTextField.getText();
        String from = fromTextField.getText();
        String to = toTextField.getText();

        if (rangeName.isEmpty() || from.isEmpty() || to.isEmpty()) {
            // Show an error message or prompt the user to fill in all fields
            return;
        }


        // Call appController to add the new range
        selectedSheetViewController.addNewRange(rangeName, from, to);

        // Clear text fields after successful operation
        newRangeNameTextField.clear();
        fromTextField.clear();
        toTextField.clear();

        // Optionally update the choice boxes
        updateChoiceBoxes();
    }


    @FXML
    void ClickMeRemoveRange(ActionEvent event) {
        if (!selectedSheetViewController.isSheetInLatestVersion()) {
            removeRangeChoiceBox.getSelectionModel().clearSelection();
            new ErrorMessage("You are not on the latest version of the sheet, so you cannot remove ranges. Please update to the latest version using the button at the top right before making changes.");
        } else {
            String selectedRange = removeRangeChoiceBox.getSelectionModel().getSelectedItem();

            if (selectedRange != null) {
                // Call appController to remove the selected range
                selectedSheetViewController.removeRange(selectedRange);
                removeRangeChoiceBox.getSelectionModel().clearSelection();
                // Update the choice boxes after removal
                updateChoiceBoxes();
            }
        }
    }


    @FXML void ClickMeShowRange(ActionEvent event) {
        String selectedRange = showRangeChoiceBox.getSelectionModel().getSelectedItem();

        if (selectedRange != null) {
            // Call appController to show/highlight the selected range
            selectedSheetViewController.selectRange(selectedRange);
            showRangeChoiceBox.getSelectionModel().clearSelection();
        }
    }



    @FXML
    private void handleDynamicAnalysis(ActionEvent event) {
        try {
            // קבלת הערכים מה-TextFields
            double minValue = Double.parseDouble(minValueField.getText());
            double maxValue = Double.parseDouble(maxValueField.getText());
            double stepSize = Double.parseDouble(stepSizeField.getText());

            //צריך לתקן
            String selectedCellKey = selectedSheetViewController.getSelectedCellKey(); // לקבל את תא שנבחר
            showDynamicAnalysisPopup(selectedCellKey, minValue, maxValue, stepSize);



        } catch (NumberFormatException e) {
            new ErrorMessage("Please enter valid numeric values.");
        }
    }

    public void showDynamicAnalysisPopup(String cellKey, double minValue, double maxValue, double stepSize) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/popup/dynamicAnalysisSheet/dynamicAnalysisSheet.fxml"));
            Parent root = loader.load();

            DynamicAnalysisSheetController controller = loader.getController();
            controller.setSelectedSheetViewController(selectedSheetViewController);

            // קריאה אסינכרונית לקבלת הגיליון המעודכן
            String currentSheetName = selectedSheetViewController.getCurrentSheetName();
            selectedSheetViewController.getDtoSheet(currentSheetName, dtoSheet -> {
                // Initialize the popup with the selected cell and range values after fetching the sheet data
                Platform.runLater(() -> {
                    controller.initDynamicAnalysisSheet(dtoSheet, cellKey, minValue, maxValue, stepSize);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Dynamic Analysis");
                    stage.show();
                });
            }, errorMessage -> {
                // במקרה של שגיאה, הצגת הודעת שגיאה למשתמש
                Platform.runLater(() -> new ErrorMessage("Failed to load sheet for dynamic analysis: " + errorMessage));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleGenerateGraph() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/popup/graph/graphSelector.fxml"));
            Parent root = loader.load();

            GraphController graphController = loader.getController();
            graphController.setSelectedSheetViewController(selectedSheetViewController);

            Stage stage = new Stage();
            stage.setTitle("Select Data for Graph");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            new component.subcomponent.popup.errormessage.ErrorMessage(e.getMessage());
        }
    }



    //************************************filter*************************


    @FXML void ClickMeFilterButton(ActionEvent event) {

        // Collect selected column values from the filter data pane
        Map<String, List<String>> selectedColumnValues =collectSelectedColumnValues();

        // Get the range (from and to) values from the input fields
        String from = filterFromTextField.getText();
        String to = filterToTextField.getText();

        // Validate that the range fields are filled correctly
        if(!validateRangeFields(from,to)){
            return; // If validation fails, exit the function
        }

        // Clear input fields after filtering
        clearFilterInputs();

        try {
            // Convert the selected column values to a JSON string
            String selectedColumnValuesJson = convertSelectedValuesToJson(selectedColumnValues);

            // Build the URL for the filtering request
            String url = buildFilterSheetUrl(from, to, selectedColumnValuesJson);

            // Send the filtering request to the server
            sendFilterRequest(url);

        } catch (Exception e) {
            // Display any error messages that occur during the process
            displayErrorMessage(e.getMessage());
        }
    }



    private Map<String, List<String>> collectSelectedColumnValues() {
        Map<String, List<String>> selectedColumnValues = new HashMap<>();

        for (Node node : filterDataFlowPane.getChildren()) {
            if (node instanceof MenuButton) {
                MenuButton menuButton = (MenuButton) node;
                String column = menuButton.getText();

                List<String> selectedValues = new ArrayList<>();
                for (MenuItem item : menuButton.getItems()) {
                    if (item instanceof CustomMenuItem) {
                        CustomMenuItem customMenuItem = (CustomMenuItem) item;
                        Node content = customMenuItem.getContent();
                        if (content instanceof CheckBox && ((CheckBox) content).isSelected()) {
                            selectedValues.add(((CheckBox) content).getText());
                        }
                    }
                }

                if (!selectedValues.isEmpty()) {
                    selectedColumnValues.put(column, selectedValues);
                }
            }
        }

        return selectedColumnValues;
    }

    private boolean validateRangeFields(String from, String to) {
        if (from.isEmpty() || to.isEmpty()) {
            Platform.runLater(() -> {
                new ErrorMessage("Please fill in the range fields.");
            });
            return false;
        }
        return true;
    }

    private void clearFilterInputs() {
        filterFromTextField.clear();
        filterToTextField.clear();
        filterDataFlowPane.getChildren().clear();
        selectColumnsToFilterByMenu.getItems().clear();
        filterButton.setDisable(true);
    }

    private String convertSelectedValuesToJson(Map<String, List<String>> selectedColumnValues) {
        Gson gson = new Gson();
        return gson.toJson(selectedColumnValues);
    }

    private String buildFilterSheetUrl(String from, String to, String selectedColumnValuesJson) {

        return FILTER_SHEET +
                "?sheetName=" + selectedSheetViewController.getFileName() +
                "&from=" + from + "&to=" + to +
                "&selectedValues=" + selectedColumnValuesJson.replace("{", "%7B").replace("}", "%7D")
                .replace("[", "%5B").replace("]", "%5D")
                .replace("\"", "%22").replace(" ", "%20");
    }

    private void sendFilterRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                displayErrorMessage("Error in filtering request: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    JsonSerializer jsonSerializer = new JsonSerializer();
                    DTOSheet filterSheet = jsonSerializer.convertJsonToDto(jsonResponse);
                    loadFilteredSheetPopup(filterSheet);
                } else {
                    displayErrorMessage("Failed to fetch filtered sheet: " + response.code());
                }
                //return null;
            }
        });
    }

    private void loadFilteredSheetPopup(DTOSheet filterSheet) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/popup/viewonlysheet/viewOnlySheet.fxml"));
                Parent root = loader.load();

                ViewOnlySheetController viewOnlySheetController = loader.getController();
                viewOnlySheetController.setAppController(selectedSheetViewController);
                viewOnlySheetController.initViewOnlySheetAndBindToUIModel(filterSheet, true);

                Stage stage = new Stage();
                stage.setTitle("Filtered Sheet");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                new ErrorMessage("Error loading filtered sheet view: " + e.getMessage());
            }
        });
    }

    private void displayErrorMessage(String message) {
        Platform.runLater(() -> {
            new ErrorMessage(message);
        });
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

    @FXML void clickMeResetFilterButton(ActionEvent event){
        filterFromTextField.clear();
        filterToTextField.clear();
        filterDataFlowPane.getChildren().clear();
        selectColumnsToFilterByMenu.getItems().clear();
        filterButton.setDisable(true);
    }



    /************************************** SORT FEATURE:*******************************************/

    @FXML void ClickMeSortButton(ActionEvent event) {

        // Get the range (from and to) values from the input fields
        String from = sortFromTextField.getText();
        String to = sortToTextField.getText();

        // Validate that the range fields are filled correctly
        if (!validateRangeFields(from, to)) {
            return;// If validation fails, exit the function
        }

        // Collect the selected columns' priorities for sorting
        List<Character> listOfColumnsPriorities = collectSortPriorities();

        // Clear the input fields after the user initiates the sorting action
        clearSortInputs();

        try {
            // Build the URL for the sort request with the required parameters
            String url = buildSortRequestUrl(from, to, listOfColumnsPriorities);

            // Send the sort request to the server and process the response
            sendSortRequest(url);
        } catch (Exception e) {
            // Display any errors encountered during the process
            displayErrorMessage(e.getMessage());
        }
    }

    // Function to collect column priorities
    private List<Character> collectSortPriorities() {
        List<Character> listOfColumnsPriorities = new ArrayList<>();
        for (Node labelNode : selectedSortColumnsFlowPane.getChildren()) {
            if (labelNode instanceof Label) {
                String labelText = ((Label) labelNode).getText();
                if (!labelText.isEmpty()) {
                    listOfColumnsPriorities.add(labelText.charAt(0));
                }
            }
        }
        return listOfColumnsPriorities;
    }
    // Function to clear input fields after sorting
    private void clearSortInputs() {
        sortFromTextField.clear();
        sortToTextField.clear();
        selectedSortColumnsFlowPane.getChildren().clear();
        selectColumnsToSortByMenu.getItems().clear();
        sortButton.setDisable(true);
    }
    // Function to build the request URL
    private String buildSortRequestUrl(String from, String to, List<Character> listOfColumnsPriorities) throws Exception {
        // Convert the list of column priorities to a string
        String prioritiesListStr = listOfColumnsPriorities.stream()
                .map(String::valueOf)
                .reduce("", (acc, item) -> acc + item);

        // Build the request URL with the necessary parameters
        return SORT_SHEET +
                "?sheetName=" + selectedSheetViewController.getFileName() +
                "&from=" + from + "&to=" + to + "&prioritiesList=" + prioritiesListStr;
    }
    // Function to send the sort request to the server
    private void sendSortRequest(String url) {
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

                Platform.runLater(() -> {
                    try {
                        loadSortedSheetPopup(sortedSheet);
                    } catch (IOException e) {
                        displayErrorMessage("Error loading sorted sheet view: " + e.getMessage());
                    }
                });

            } else {
                displayErrorMessage("Failed to fetch sheet: " + response.code());
            }

        } catch (IOException e) {
            displayErrorMessage(e.getMessage());
        }
    }
    // Function to load the sorted sheet in a new window
    private void loadSortedSheetPopup(DTOSheet sortedSheet) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/popup/viewonlysheet/viewOnlySheet.fxml"));
        Parent root = loader.load();

        ViewOnlySheetController viewOnlySheetController = loader.getController();
        viewOnlySheetController.setAppController(selectedSheetViewController);
        viewOnlySheetController.initViewOnlySheetAndBindToUIModel(sortedSheet, true);

        Stage stage = new Stage();
        stage.setTitle("Sorted Sheet");
        stage.setScene(new Scene(root));
        stage.show();
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

    @FXML void clickMeResetSortButton(ActionEvent event) {
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

        // Validate that the range fields are filled correctly
        if (!validateRangeFields(fromText, toText)) {
            return;// If validation fails, exit the function
        }

        List<String> columnsInRange = getColumnsInRange(fromText, toText);
        int firstRowInRange = getFirstRowInRange(fromText);
        int lastRowInRange = getLastRowInRange(toText);

        if (columnsInRange == null) {
            displayErrorMessage("Invalid input! Please enter a valid range.");
        }

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

    public void startRangePolling() {
        rangesPollimgTimer = new Timer(true); // Polling will run as a daemon thread
        rangesPollimgTimer.schedule(new RangePollerTask(this, selectedSheetViewController.getAllRanges()), 0, 5000); // Poll every 5 seconds
    }



    //?????????????????????????????????????????????????
    @Override
    public void close() throws IOException {
        if (rangesPollimgTimer != null) {
            rangesPollimgTimer.cancel();
        }
    }

    public void stopRangePolling() {
        if (rangesPollimgTimer != null) {
            rangesPollimgTimer.cancel();
        }
    }
}
