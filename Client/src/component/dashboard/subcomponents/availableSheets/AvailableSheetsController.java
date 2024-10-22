package component.dashboard.subcomponents.availableSheets;

import JsonSerializer.JsonSerializer;
import component.dashboard.main.maindashboard.DashboardController;
import dto.DTOSheetInfo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import util.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

import static util.Constants.*;

public class AvailableSheetsController implements Closeable {

    private DashboardController dashboardController;
    private Timer refreshTimer;
    private TimerTask listRefresher;


    @FXML private TableView<AvailableSheetRow> availableSheetTable;
    private ObservableList<AvailableSheetRow> sheetRows = FXCollections.observableArrayList();

    @FXML private VBox permissionsTable;


    public void initialize()   {

        // Disable the built-in row selection mechanism
        availableSheetTable.setSelectionModel(null);

        // Set the items for the availableSheetTable
        availableSheetTable.setItems(sheetRows);

        // Call helper function to configure row behavior (color and checkbox behavior)
        configureSheetTableRowBehavior();

        // Load available sheets from server when initializing
        availableSheetTable.getStylesheets().add(getClass().getResource("availableSheetsTable.css").toExternalForm());

        // Load available sheets from server when initializing
        loadAvailableSheetsFromServer();

        // Start the sheet refresher
        availableSheetRefresher();
    }

    // Load available sheets from the server
    private void loadAvailableSheetsFromServer()  {
        String url = AVAILABLE_SHEETS;

        try{
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
            Response response = call.execute();



            if (response.isSuccessful()) {
                        // Convert response to string
                String jsonResponse = response.body().string();
                try {
                    JsonSerializer jsonSerializer = new JsonSerializer();
                    List<DTOSheetInfo> dtoSheetInfoList = jsonSerializer.convertJsonToDtoInfoList(jsonResponse);

                    for (DTOSheetInfo dtoSheetInfo : dtoSheetInfoList) {
                        String fileName = dtoSheetInfo.getSheetName();
                        String sheetSize =  dtoSheetInfo.getNumRows() + "x" + dtoSheetInfo.getNumCols();
                        String uploadedBy = dtoSheetInfo.getUploadBy();

                        AvailableSheetRow row = new AvailableSheetRow(fileName, sheetSize, uploadedBy, this);
                        // Add the row to the sheetRows list, which will update the table.
                        sheetRows.add(row);
                    }
                }catch (Exception e){
                    System.out.println("Error parsing JSON response: " + e.getMessage());
                }

            } else {
                System.out.println("Error: " + response.code()); // Handle unsuccessful response
            }
        } catch (IOException e) {
            System.out.println("Error communicating with the server: " + e.getMessage());
        }
    }



    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void addSheetToAvailableSheetTable(AvailableSheetRow row) {
        sheetRows.add(row);
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }

    public String getSelectedSheetName() {

        for (AvailableSheetRow row : sheetRows) { // Assuming sheetRows is your ObservableList for the TableView
            if (row.getSelected().isSelected()) {
                return row.getSheetName(); // Return the sheet name of the selected row
            }
        }
        return null; // Return null if no sheet is selected
    }


    public void availableSheetRefresher() {
        refreshTimer = new Timer(true); // Create a daemon timer
        AvailableSheetsRefresher refresher = new AvailableSheetsRefresher(this); // Pass the controller instance
        refreshTimer.schedule(refresher, REFRESH_RATE, REFRESH_RATE); // Run every 2 seconds
    }

    public void refreshAvailableSheets(List<DTOSheetInfo> dtoSheetInfoList) {

        // Get the current sheets in the table (by sheet name)
        List<String> currentSheetNames = availableSheetTable.getItems().stream()
                .map(AvailableSheetRow::getSheetName)
                .toList();

        // Go through each new sheet and add it to the table only if it doesn't exist already
        for (DTOSheetInfo dtoSheetInfo : dtoSheetInfoList) {
            String sheetName = dtoSheetInfo.getSheetName();

            // Check if the sheet name is already in the table
            if (!currentSheetNames.contains(sheetName)) {
                // Create a new row for the sheet
                String sheetSize = dtoSheetInfo.getNumRows() + "x" + dtoSheetInfo.getNumCols();
                String uploadedBy = dtoSheetInfo.getUploadBy();

                AvailableSheetRow newRow = new AvailableSheetRow(sheetName, sheetSize, uploadedBy, this);

                // Add the new row to the table
                availableSheetTable.getItems().add(newRow);
            }
        }

    }


    // Helper function to deselect all checkboxes except the one selected
    public void handleCheckBoxSelection(CheckBox selectedCheckBox) {
        for (AvailableSheetRow row : availableSheetTable.getItems()) {
            if (row.getSelected() != selectedCheckBox) {
                row.getSelected().setSelected(false);
            }
        }
    }

    // Helper function to configure the row behavior in the availableSheetTable
    private void configureSheetTableRowBehavior() {
        availableSheetTable.setRowFactory(tv -> new TableRow<AvailableSheetRow>() {
            @Override
            protected void updateItem(AvailableSheetRow item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle(""); // Clear any custom style when no item or row is empty
                } else {
                    CheckBox checkBox = item.getSelected();

                    // Add listener to change row color when checkbox is selected
                    checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            setStyle("-fx-border-color: black; -fx-border-width: 2px;"); // Highlight the row
                        } else {
                            setStyle(""); // Remove the style when deselected
                        }
                    });

                    // Allow row click to toggle checkbox selection
                    setOnMouseClicked(event -> {
                        if (!isEmpty()) {
                            checkBox.setSelected(!checkBox.isSelected());
                        }
                    });
                }
            }
        });
    }


    //?????????????????????????????????????????????????
    @Override
    public void close() throws IOException {
        if(refreshTimer != null) {
            refreshTimer.cancel();
        }
        if(listRefresher != null) {
            listRefresher.cancel();
        }
    }
}
;
