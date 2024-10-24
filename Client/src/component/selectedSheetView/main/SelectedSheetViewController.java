package component.selectedSheetView.main;

import JsonSerializer.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import component.main.SheetCellAppMainController;
import component.popup.error.ErrorMessage;
import component.selectedSheetView.subcomponent.header.SelectedSheetViewHeaderController;
import component.selectedSheetView.subcomponent.left.SelectedSheetViewLeftController;
import component.selectedSheetView.subcomponent.sheet.CellStyle;
import component.selectedSheetView.subcomponent.sheet.SelectedSheetController;
import component.selectedSheetView.subcomponent.sheet.UIModelSheet;
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
import util.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.Constants.*;

/**
 * This class is the main controller for the selected sheet view.
 * It handles interactions between different subcomponents (header, sheet, and left pane),
 * manages cell selection, and provides the main logic for user interactions.
 */
public class SelectedSheetViewController implements Closeable {

    private SheetCellAppMainController sheetCellAppMainController;
    private String selectedSheetName;

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
            //startPolling();
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

                 //Check if the cell contains a numeric value
                 isNumericCellSelected.setValue(isNumeric(sheetController.getOriginalValue(newCellId)));
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
     * Highlights and displays the selected range.
     * @param selectedRange the name of the range to display.
     */
    public void showRange(String selectedRange) {
        clearPreviousSelection();  // Clear previous selections
        try {
            // Send a GET request to the server to fetch the coordinates list for the selected range
            String finalUrl = HttpUrl.parse(RANGE)  // Server URL for the range
                    .newBuilder()
                    .addQueryParameter("rangeName", selectedRange)
                    .build()
                    .toString();

            Request request = new Request.Builder()
                    .url(finalUrl)
                    .build();

            // Send the request to the server
            HttpClientUtil.HTTP_CLIENT.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Platform.runLater(() -> new ErrorMessage("Failed to fetch range: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        // Convert the response to a List of cell IDs
                        String jsonResponse = response.body().string();
                        List<String> cellsId = GSON_INSTANCE.fromJson(jsonResponse, new TypeToken<List<String>>(){}.getType());

                        // Add the border for each cell based on the cell IDs
                        Platform.runLater(() -> {
                            try {
                                sheetController.addBorderForCells(
                                        CellStyle.RANGE_CELL_BORDER_COLOR.getColorValue(),
                                        CellStyle.RANGE_CELL_BORDER_STYLE.getStyleValue(),
                                        CellStyle.RANGE_CELL_BORDER_WIDTH.getWidthValue(),
                                        cellsId);
                                previouslySelectedCells.addAll(cellsId);  // Store the previously selected cells
                            } catch (Exception e) {
                                new ErrorMessage(e.getMessage());
                            }
                        });
                    } else {
                        Platform.runLater(() -> new ErrorMessage("Failed to fetch range: " + response.message()));
                    }
                    //return null;
                }
            });
        } catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }
    }


    /**
     * Adds a new range to the sheet based on the given parameters.
     * @param rangeName the name of the range.
     * @param from the starting cell of the range.
     * @param to the ending cell of the range.
     */
    public void addNewRange(String rangeName, String from, String to) {
        try {
            // Build the final URL for the request
            String finalUrl = HttpUrl
                    .parse(RANGES)
                    .newBuilder()
                    .build()
                    .toString();

            // Create the JSON body for the request
            String jsonBody = "{\"newRangeName\": \"" + rangeName + "\", \"fromCoordinate\": \"" + from + "\", \"toCoordinate\": \"" + to + "\"}";

            // Build the request body
            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));

            // Create the PUT request
            Request request = new Request.Builder()
                    .url(finalUrl)
                    .put(body)
                    .build();

            // Execute the request synchronously
            Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
            Response response = call.execute();

            if (response.isSuccessful()) {
                response.close(); // Close response body
            } else {
                new ErrorMessage("Failed to add new range: " + response.message());
                response.close(); // Close response body
            }
        } catch (IOException e) {
            new ErrorMessage(e.getMessage());
        }
    }


    /**
     * Removes a selected range from the sheet.
     * @param selectedRange the name of the range to remove.
     */
    public void removeRange(String selectedRange) {
        try {
            // Build the final URL for the request
            String finalUrl = HttpUrl
                    .parse(RANGES)
                    .newBuilder()
                    .build()
                    .toString();

            // Create the JSON body for the request
            String jsonBody = "{\"rangeName\": \"" + selectedRange + "\"}";

            // Build the request body
            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));

            // Create the DELETE request
            Request request = new Request.Builder()
                    .url(finalUrl)
                    .delete(body)
                    .build();

            // Execute the request synchronously
            Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
            Response response = call.execute();

            if (response.isSuccessful()) {
                selectedRangeId.set(selectedRange);
                clearPreviousSelection();  // Clear the UI selection
                response.close(); // Close response body
            } else {
                new ErrorMessage("Failed to remove range: " + response.message());
                response.close(); // Close response body
            }
        } catch (IOException e) {
            new ErrorMessage(e.getMessage());
        }
    }






    /**
     * Clears the previous selection from the sheet.
     */
    private void clearPreviousSelection() {
        if (!previouslySelectedCells.isEmpty()) {
            sheetController.addBorderForCells(CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue(),
                    CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue(),
                    CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue(),
                    previouslySelectedCells);
            previouslySelectedCells.clear();
        }
    }

    /**
     * Shows the data for the newly selected cell.
     *
     * @param cellId the ID of the newly selected cell.
     */
    private void showCellData(String cellId) {
        clearPreviousSelection();

        List<String> selectedCell = new ArrayList<>();
        selectedCell.add(cellId);

        sheetController.addBorderForCells(
                CellStyle.SELECTED_CELL_BORDER_COLOR.getColorValue(),
                CellStyle.SELECTED_CELL_BORDER_STYLE.getStyleValue(),
                CellStyle.SELECTED_CELL_BORDER_WIDTH.getWidthValue(), selectedCell);

        headerController.updateHeaderValues(cellId.replace(":", ""), sheetController.getOriginalValue(selectedCellId.getValue()),
                String.valueOf(sheetController.getLastModifiedVersion(selectedCellId.getValue())),
                sheetController.getCellTextColor(selectedCellId.getValue()),
                sheetController.getCellBackgroundColor(selectedCellId.getValue()),
                sheetController.getCellWidth(selectedCellId.getValue()),
                sheetController.getCellHeight(selectedCellId.getValue()),
                sheetController.getCellAligmentString(selectedCellId.getValue()),
                sheetController.getCellEditorName(selectedCellId.getValue()));

        // Highlight dependencies and influenced cells
        List<String> DependsOnCellsId = sheetController.getDependsOn(selectedCellId.getValue());
        sheetController.addBorderForCells(CellStyle.DEPENDS_ON_CELL_BORDER_COLOR.getColorValue(),
                CellStyle.DEPENDS_ON_CELL_BORDER_STYLE.getStyleValue(),
                CellStyle.DEPENDS_ON_CELL_BORDER_WIDTH.getWidthValue(), DependsOnCellsId);

        List<String> InfluencingOnCellsId = sheetController.getInfluencingOn(selectedCellId.getValue());
        sheetController.addBorderForCells(CellStyle.INFLUENCING_ON_CELL_BORDER_COLOR.getColorValue(),
                CellStyle.INFLUENCING_ON_CELL_BORDER_STYLE.getStyleValue(),
                CellStyle.INFLUENCING_ON_CELL_BORDER_WIDTH.getWidthValue(), InfluencingOnCellsId);

        // Add the cells to the list for clearing later
        previouslySelectedCells.add(cellId);
        previouslySelectedCells.addAll(DependsOnCellsId);
        previouslySelectedCells.addAll(InfluencingOnCellsId);
    }



    /**
     * Unselects the given cell and clears its data from the header.
     * @param cellId the ID of the cell to unselect.
     */
    public void unShowCellData(String cellId) {
        headerController.updateHeaderValues("", "", "",
                CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue(),
                CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue(),
                sheetController.getCellWidth(selectedCellId.getValue()),
                sheetController.getCellHeight(selectedCellId.getValue()),
                sheetController.getCellAligmentString(selectedCellId.getValue()),
                "");

        clearPreviousSelection();
    }

    /**
     * Displays the loaded sheet in the UI.
     */
    public void displaySheet(DTOSheet dtoSheet,String fileName) {
        // Implementation for displaying the sheet
        sheetController.initSheetAndBindToUIModel(dtoSheet,fileName);
        this.selectedSheetName = fileName;
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
     * Returns a list of all ranges in the sheet asynchronously.
     * @param callback a callback function to handle the list of ranges when the request completes.
     */
    /**
     * Returns a list of all ranges in the sheet.
     * @return a list of all ranges.
     */
    public List<String> getAllRanges() {
        List<String> rangesName = new ArrayList<>();
        try {
            // Define the URL for fetching all ranges
            String finalUrl = HttpUrl.parse(RANGES).newBuilder().build().toString();

            // Create the GET request
            Request request = new Request.Builder()
                    .url(finalUrl)
                    .build();

            // Send the request synchronously to get the response (note: this blocks the thread)
            Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();

            if (response.isSuccessful()) {
                // Parse the JSON response to get the list of range names
                String jsonResponse = response.body().string();
                rangesName = GSON_INSTANCE.fromJson(jsonResponse, new TypeToken<List<String>>() {}.getType());
                response.body().close();  // Close the response body after use
            } else {
                // Handle failure response from the server
                new ErrorMessage("Failed to fetch ranges: " + response.message());
            }
        } catch (IOException e) {
            // Handle exceptions
            new ErrorMessage(e.getMessage());
        }
        return rangesName;
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
     * @param cellId the ID of the cell to select.
     */
    public void selectCell(String cellId) {
        selectedCellId.set(cellId);  // Triggers the listener
    }

    public void updateCellValue(String newOriginalValue) {
        try {
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

            // Send the request synchronously
            try (Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String sheetName = response.body().string();
                    DTOSheet dtoSheet = getDtoSheet(sheetName);

                    if(dtoSheet!=null) {
                        // Update the sheet in the UI using Platform.runLater
                        Platform.runLater(() -> sheetController.updateSheetValues(dtoSheet));
                    }

                    // Handle successful update
                    System.out.println("Cell updated successfully");
                } else {
                    // Handle error response
                    String responseBody = response.body().string();
                    Platform.runLater(() -> new ErrorMessage("Update failed: " + responseBody));
                }
            }
        } catch (IOException e) {
            // Handle failure
            Platform.runLater(() -> new ErrorMessage("Failed to update cell: " + e.getMessage()));
        }
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
                new ErrorMessage("Failed to fetch sheet: " + response.body().string());
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


    public List<String> getColumnValues(char column, int firstRow, int lastRow) {
        return sheetController.getColumnValues(column, firstRow, lastRow);
    }

    public UIModelSheet getCurrentUIModel() {
        return sheetController.getCurrentUIModel();

    }

    public String getFileName() {
        return this.selectedSheetName;
    }


    public void applyTheme(String style) {
        sheetCellAppMainController.applyTheme(style);
    }

    /**
     * Changes the selected range and triggers the listener for range selection.
     * @param rangeId the ID of the range to select.
     */
    public void selectRange(String rangeId) {
        selectedRangeId.set(rangeId);  // Triggers the listener
    }

    public void updateChoiceBoxes() {
        leftController.updateChoiceBoxes();
    }

    public void startRangePolling(){
        leftController.startRangePolling();
    }

    public void startSheetPolling(){sheetController.startPolling();}

    /**
     * Temporarily updates the value of a cell for dynamic analysis.
     *
     * @param cellKey  the key of the cell to update.
     * @param newValue the new value to set.
     * @return the updated DTOSheet object, or null if an error occurred.
     */
    public DTOSheet updateTemporaryCellValue(String cellKey, double newValue) {
        String url = DYNAMIC_ANALYSIS + "?coordinate=" + cellKey + "&value=" + String.valueOf(newValue);

        // יצירת בקשת GET
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            // שליחת הבקשה באופן סינכרוני
            Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();

            // בדיקת הצלחת הבקשה
            if (response.isSuccessful()) {
                // קבלת התשובה מהשרת והמרה ל-DTOSheet
                String jsonResponse = response.body().string();
                JsonSerializer jsonSerializer = new JsonSerializer();
                DTOSheet dtoSheet = jsonSerializer.convertJsonToDto(jsonResponse);

                // סגירת body של התשובה
                response.body().close();

                // החזרת ה-DTOSheet
                return dtoSheet;
            } else {
                System.err.println("Failed to update cell for dynamic analysis: " + response.message());
                return null;
            }
        } catch (IOException e) {
            System.err.println("Failed to send request for dynamic analysis: " + e.getMessage());
            return null;
        }
    }



    /**
     * Checks if a string contains a numeric value.
     * @param str the string to check.
     * @return true if the string is numeric, false otherwise.
     */
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns the currently selected cell key.
     * @return the selected cell key.
     */
    public String getSelectedCellKey() {
        return selectedCellId.getValue();
    }

   //???????????????????????????????????????????????????
    @Override
    public void close() throws IOException {
//        if(sheetPollingTimer!=null){
//            sheetPollingTimer.cancel();
//        }
        sheetController.close();
        leftController.close();
    }
}
