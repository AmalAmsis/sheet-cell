package component.selectedSheetView.subcomponent.left;

import component.selectedSheetView.main.SelectedSheetViewController;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.util.List;

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
     * Updates the menu items for sorting or filtering.
     *
     * @param menu The menu to update.
     * @param flowPane The flow pane that holds the selected columns.
     * @param fromTextField The starting range text field.
     * @param toTextField The ending range text field.
     */
    private void updateMenuItems(MenuButton menu, FlowPane flowPane, TextField fromTextField, TextField toTextField) {
        // Implementation of menu item updates
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

    @FXML
    void ClickMeAddNewRangeButton(ActionEvent event) {

    }

    @FXML
    void ClickMeFilterButton(ActionEvent event) {

    }

    @FXML
    void ClickMeRemoveRange(ActionEvent event) {

    }

    @FXML
    void ClickMeShowRange(ActionEvent event) {

    }

    @FXML
    void ClickMeSortButton(ActionEvent event) {

    }

    @FXML
    void clickMeResetFilterButton(ActionEvent event) {

    }

    @FXML
    void clickMeResetSortButton(ActionEvent event) {

    }

    @FXML
    void handleDynamicAnalysis(ActionEvent event) {

    }

    @FXML
    void handleGenerateGraph(ActionEvent event) {

    }
}
