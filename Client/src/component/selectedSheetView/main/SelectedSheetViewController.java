package component.selectedSheetView.main;

import JsonSerializer.JsonSerializer;
import component.main.SheetCellAppMainController;
import component.popup.error.ErrorMessage;
import component.selectedSheetView.subcomponent.header.SelectedSheetViewHeaderController;
import component.selectedSheetView.subcomponent.left.SelectedSheetViewLeftController;
import component.selectedSheetView.subcomponent.sheet.SelectedSheetController;
import component.selectedSheetView.subcomponent.sheetPoller.SheetPollerTask;
import constants.Constants.*;
import dto.DTOSheet;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static util.Constants.*;

/**
 * This class is the main controller for the selected sheet view.
 * It handles interactions between different subcomponents (header, sheet, and left pane),
 * manages cell selection, and provides the main logic for user interactions.
 */
public class SelectedSheetViewController {

    private SheetCellAppMainController sheetCellAppMainController;

    @FXML
    private ScrollPane header;
    @FXML
    private SelectedSheetViewHeaderController headerController;
    @FXML
    private ScrollPane sheet;
    @FXML
    private SelectedSheetController sheetController;
    @FXML
    private ScrollPane left;
    @FXML
    private SelectedSheetViewLeftController leftController;

    private Stage primaryStage;

    // Properties to track selected cell and range
    private ObjectProperty<String> selectedCellId = new SimpleObjectProperty<>();
    private ObjectProperty<String> selectedRangeId = new SimpleObjectProperty<>();

    // Boolean properties to manage the state of selection and file loading
    private BooleanProperty isSelected = new SimpleBooleanProperty();
    private BooleanProperty isNumericCellSelected = new SimpleBooleanProperty();

    // List to store previously selected cells for clearing their state
    private List<String> previouslySelectedCells = new ArrayList<>();

    private Timer sheetPollingTimer;

    /**
     * Initializes the controller and sets up bindings for UI components and properties.
     * Ensures that the sub-controllers are properly linked to the main controller.
     */
    @FXML
    public void initialize() {
        // Ensure that the sub-controllers are set up properly
        if (headerController != null && sheetController != null && leftController != null) {
            headerController.setSelectedSheetViewController(this);
            sheetController.setSelectedSheetViewController(this);
            leftController.setSelectedSheetViewController(this);

            // Start polling when the sheet is opened
            startPolling();
        }

        // Initialize properties
        isSelected.setValue(false);
        isNumericCellSelected.setValue(false);

        // Bind properties to UI components
        headerController.bindingToIsSelected(isSelected);
        leftController.bindingToNumericCellIsSelected(isNumericCellSelected);

        // Listener for selected cell changes
        selectedCellId.addListener((observable, oldCellId, newCellId) -> {
            if (oldCellId != null) {
                unShowCellData(oldCellId);  // Unselect old cell
            }
            if (newCellId != null) {
                showCellData(newCellId);  // Select new cell
                isSelected.setValue(true);

                // Check if the cell contains a numeric value (commented out for future implementation)
                // DTOCell dtoCell = uiManager.getDtoCellForDisplayCell(newCellId.replace(":", ""));
                // isNumericCellSelected.setValue(isNumeric(dtoCell.getOriginalValue()));
            }
        });

        // Listener for selected range changes
        selectedRangeId.addListener((observable, oldRangeId, newRangeId) -> {
            if (oldRangeId != null) {
                clearPreviousSelection();  // Clear old range
            }
            if (newRangeId != null) {
                showRange(newRangeId);  // Show new range
                isSelected.setValue(false);
                isNumericCellSelected.setValue(false);
            }
        });
    }

    public void setSheetCellAppMainController(SheetCellAppMainController sheetCellAppMainController) {
        this.sheetCellAppMainController = sheetCellAppMainController;
    }

    /**
     * Displays the selected range in the sheet.
     *
     * @param newRangeId the ID of the new range to display.
     */
    private void showRange(String newRangeId) {
        // Implementation to display the selected range
    }

    /**
     * Clears the previous selection from the sheet.
     */
    private void clearPreviousSelection() {
        // Implementation to clear the previous selection
    }

    /**
     * Shows the data for the newly selected cell.
     *
     * @param newCellId the ID of the newly selected cell.
     */
    private void showCellData(String newCellId) {
        // Implementation to show data for the new cell
    }

    /**
     * Unselects the old cell and removes its data from the UI.
     *
     * @param oldCellId the ID of the old cell to unselect.
     */
    private void unShowCellData(String oldCellId) {
        // Implementation to unselect the old cell
    }

    /**
     * Displays the loaded sheet in the UI.
     */
    public void displaySheet(DTOSheet dtoSheet) {
        // Implementation for displaying the sheet
        sheetController.initSheetAndBindToUIModel(dtoSheet);
    }

    /**
     * Sets the background color of the currently selected cell.
     *
     * @param backgroundColor the new background color to apply.
     */
    public void setSelectedCellBackgroundColor(Color backgroundColor) {
        sheetController.setCellBackgroundColor(selectedCellId.getValue(), backgroundColor);
    }

    /**
     * Sets the text color of the currently selected cell.
     *
     * @param textColor the new text color to apply.
     */
    public void setSelectedCellTextColor(Color textColor) {
        sheetController.setCellTextColor(selectedCellId.getValue(), textColor);
    }

    /**
     * Sets the width of the column for the currently selected cell.
     *
     * @param newVal the new width to apply.
     */
    public void setSelectedColumnWidth(Integer newVal) {
        int colIndex = selectedCellId.getValue().charAt(0) - 'A' + 1;
        sheetController.setColumnWidth(colIndex, newVal);
    }

    /**
     * Sets the height of the row for the currently selected cell.
     *
     * @param newVal the new height to apply.
     */
    public void setSelectedRowHeight(Integer newVal) {
        String[] parts = selectedCellId.getValue().split(":");
        int row = Integer.parseInt(parts[1]);
        sheetController.setRowHeight(row, newVal);
    }

    /**
     * Sets the text alignment for the column of the currently selected cell.
     *
     * @param alignment the new alignment to apply.
     */
    public void setSelectedColumnAlignment(Pos alignment) {
        int colIndex = selectedCellId.getValue().charAt(0) - 'A' + 1;
        sheetController.setColumnAlignment(colIndex, alignment);
    }

    /**
     * Retrieves all available ranges for the current sheet.
     *
     * @return a list of all range IDs.
     */
    public List<String> getAllRanges() {
        List<String> allRanges = new ArrayList<>();
        // Implementation for retrieving ranges (placeholder)
        return allRanges;
    }


    public void backToDashboard() {
        sheetCellAppMainController.switchToDashboard();
    }

    /**
     * Selects the same cell again, mainly for re-displaying its data.
     */
    public void SelectSameCell() {
        showCellData(selectedCellId.getValue());
    }

    /**
     * Changes the selected cell and triggers the listener for cell selection.
     *
     * @param cellId the ID of the cell to select.
     */
    public void selectCell(String cellId) {
        selectedCellId.set(cellId);  // Triggers the listener
    }

    public void updateCellValue(String newOriginalValue) {
        // Build the final URL for the request
        String finalUrl = HttpUrl
                .parse(UPDATE_CELL)
                .newBuilder()
                .build()
                .toString();

        // Create the JSON body for the request
        String jsonBody = "{\"coordinate\": \"" + selectedCellId.getValue() + "\", \"originalValue\": \"" + newOriginalValue + "\"}";

        // Build the request body
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));

        // Create the PUT request
        Request request = new Request.Builder()
                .url(finalUrl)
                .put(body)
                .build();

        // Send the request asynchronously
        HttpClientUtil.HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Handle failure
                new ErrorMessage(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String sheetName = response.body().string();
                    DTOSheet dtoSheet = getDtoSheet(sheetName);
                    Platform.runLater(() -> {sheetController.updateSheetValues(dtoSheet);});

                    // Handle successful update
                    System.out.println("Cell updated successfully");
                } else {
                    // Handle error response
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {new ErrorMessage("Update failed: " + responseBody);});
                }
            }
        });
    }


    public DTOSheet getDtoSheet(String sheetName) {

        String fileName = sheetName;
        String username = "lo_user";  // שם המשתמש הקבוע

        String url = VIEW + "?username=" + username + "&sheetName=" + fileName;

        // יצירת בקשת GET
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // שליחת הבקשה
        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        try {
            Response response = call.execute();

            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();

                JsonSerializer jsonSerializer = new JsonSerializer();
                DTOSheet dtoSheet = jsonSerializer.convertJsonToDto(jsonResponse);
                return dtoSheet;
            } else {
                new ErrorMessage("Failed to fetch sheet: " + response.code());
            }

        } catch (IOException e) {
            new ErrorMessage("Error fetching sheet: " + e.getMessage());
        }
        return null;
    }


    public String getCurrentSheetName(){

        // יצירת בקשת GET
        Request request = new Request.Builder()
                .url(CURRENT_SHEET_NAME)
                .get()
                .build();

        // שליחת הבקשה
        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        try {
            Response response = call.execute();

            if (response.isSuccessful()) {
                String sheetName = response.body().string();
                return sheetName;

            } else {
                new ErrorMessage("Failed to find sheet: " + response.code());
            }

        } catch (IOException e) {
            new ErrorMessage("Error: " + e.getMessage());
        }
        return null;

    }

    public void updateSheet(DTOSheet dtoSheet) {
        sheetController.updateSheetValues(dtoSheet);

    }


    public Button getSwitchToTheLatestVersionButton() {
        return headerController.getSwitchToTheLatestVersionButton();
    }

    // Method to start the polling
    public void startPolling() {
        // Create a new Timer
        Timer sheetPollingTimer = new Timer();

        Button switchToTheLatestVersionButton = getSwitchToTheLatestVersionButton();
        // Schedule the SheetPollerTask to run every 10 seconds
        SheetPollerTask pollerTask = new SheetPollerTask(switchToTheLatestVersionButton);
        sheetPollingTimer.schedule(pollerTask, 0, 2000);  // Run every 2 seconds
    }

    // Stop the polling when the user leaves the sheet
    public void stopPolling() {
        if (sheetPollingTimer != null) {
            sheetPollingTimer.cancel();  // Stop the timer when no longer needed
        }
    }
}
