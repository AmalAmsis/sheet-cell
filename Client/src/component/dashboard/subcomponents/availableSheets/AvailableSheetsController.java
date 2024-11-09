package component.dashboard.subcomponents.availableSheets;

import JsonSerializer.JsonSerializer;
import component.dashboard.main.maindashboard.DashboardController;
import component.popup.error.ErrorMessage;
import dto.DTOPermissionRequest;
import dto.DTOSheetInfo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import permission.sheetPermission.PermissionRequest;
import util.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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


    @FXML private TableView<PermissionRow> permissionSheetTable;
    private ObservableList<PermissionRow> permissionRows = FXCollections.observableArrayList();

    // Define columns for the permissions table
    @FXML private TableColumn<PermissionRow, String> userNameColumn;
    @FXML private TableColumn<PermissionRow, String> permissionTypeColumn;
    @FXML private TableColumn<PermissionRow, String> approvedByOwnerColumn;


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

        // Initialize the permissions table (permissionSheetTable)
        permissionSheetTable.setItems(permissionRows);

        // Set up the columns for the permissions table
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        permissionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("permissionType"));
        approvedByOwnerColumn.setCellValueFactory(new PropertyValueFactory<>("approvedByOwner"));
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

                    // Ensure only one checkbox is selected at a time
                    checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            // Deselect all other checkboxes
                            for (AvailableSheetRow row : availableSheetTable.getItems()) {
                                if (row != item) {
                                    row.getSelected().setSelected(false);
                                }
                            }
                            setStyle("-fx-border-color: black; -fx-border-width: 2px;"); // Highlight the selected row
                            // Send permission request for the selected sheet
                            sendSheetPermissionRequest(item.getSheetName());
                        } else {
                            setStyle(""); // Remove the style when deselected
                        }
                    });


                    // Allow row click to toggle checkbox selection
                    setOnMouseClicked(event -> {
                        if (!isEmpty()) {
                            if (checkBox.isSelected()) {
                                // Deselect the checkbox if the row is already selected
                                checkBox.setSelected(false);
                            } else {
                                // Set this checkbox to selected
                                checkBox.setSelected(true);
                            }
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

    public void sendSheetPermissionRequest(String sheetName) {
        String url = SHEET_PERMISSION + "?sheetName=" + sheetName;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    new ErrorMessage("Failed to get permissions: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();

                    Platform.runLater(() -> {
                        handlePermissionResponse(jsonResponse);
                    });
                } else {
                    Platform.runLater(() -> {
                        new ErrorMessage("Failed to retrieve permissions. Server response code: " + response.code());
                    });
                }
            }
        });
    }

    public void handlePermissionResponse(String jsonResponse) {
        try {
            JsonSerializer jsonSerializer = new JsonSerializer();
            Map<String, DTOPermissionRequest> permissionsMap = jsonSerializer.convertJsonToPermissionsMap(jsonResponse);

            // Clear current rows in the permissions table
            permissionRows.clear();

            // Populate permissionRows with data from the permissionsMap
            for (Map.Entry<String, DTOPermissionRequest> entry : permissionsMap.entrySet()) {
                String userName = entry.getKey();
                DTOPermissionRequest dtoPermissionRequest = entry.getValue();

                String permissionType = dtoPermissionRequest.getType();
                String approvedByOwner = dtoPermissionRequest.getStatus();

                // Create a new PermissionRow and add it to permissionRows
                PermissionRow permissionRow = new PermissionRow(userName, permissionType, approvedByOwner);
                permissionRows.add(permissionRow);// Make sure permissionRows is linked to the table

                if (dtoPermissionRequest.getNewRequestType() !=null){
                    PermissionRow secondPermissionRow = new PermissionRow(userName, dtoPermissionRequest.getNewRequestType(), "PENDING");
                    permissionRows.add(secondPermissionRow);// Make sure permissionRows is linked to the table

                }
            }

            // Set the items of the permission table
            permissionSheetTable.setItems(permissionRows);


        } catch (Exception e) {
            Platform.runLater(() -> new ErrorMessage("Error parsing permissions data: " + e.getMessage()));
        }
    }


    public List<AvailableSheetRow> getAvailableSheetRows() {
        return availableSheetTable.getItems();
    }

};
