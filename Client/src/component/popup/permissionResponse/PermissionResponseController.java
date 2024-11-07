package component.popup.permissionResponse;

import JsonSerializer.JsonSerializer;
import com.google.gson.Gson;
import component.popup.error.ErrorMessage;
import component.popup.message.GeneralMessage;
import dto.DTOPermissionRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import okhttp3.*;
import util.http.HttpClientUtil;
import javafx.stage.Stage;

import javafx. scene. paint. Paint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

import static util.Constants.PERMISSION_UPDATE;
import static util.Constants.SHEET_PERMISSION;

public class PermissionResponseController {



    String sheetName;
    private ObservableList<permissionResponseRow> pendingRequests = FXCollections.observableArrayList();
    private static final String APPROVED = "APPROVED";
    private static final String DENIED = "DENIED";

    @FXML
    private Button OKButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TableView<permissionResponseRow> permissionRequestTable;
    @FXML
    private TableColumn<permissionResponseRow, String> userNameColumn;
    @FXML
    private TableColumn<permissionResponseRow, String> permissionTypeColumn;
    @FXML
    private TableColumn<permissionResponseRow, MenuButton> approvalColumn;
    @FXML
    private Label sheetNameLabel;

    // Initialize popup with sheet name
    public void initializePopup(String sheetName) {
        this.sheetName = sheetName;
        sheetNameLabel.setText("Sheet Name: " + sheetName);
        fetchPendingRequests();
    }

    // Fetch PENDING requests from the server and add to the table
    private void fetchPendingRequests() {
        String url = SHEET_PERMISSION + "?sheetName=" + sheetName;
        Request request = new Request.Builder().url(url).get().build();

        HttpClientUtil.HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> new ErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    JsonSerializer jsonSerializer = new JsonSerializer();
                    Map<String, DTOPermissionRequest> permissionsMap = jsonSerializer.convertJsonToPermissionsMap(jsonResponse);

                    Platform.runLater(() -> {
                        for (Map.Entry<String, DTOPermissionRequest> entry : permissionsMap.entrySet()) {
                            DTOPermissionRequest dto = entry.getValue();
                            if ("PENDING".equals(dto.getStatus())) {

                                MenuButton barMenu = createApprovalMenu();
                                pendingRequests.add(new permissionResponseRow(entry.getKey(), dto.getType(), barMenu));
                            }
                            else if(dto.getNewRequestType()!=null) {
                                MenuButton barMenu2 = createApprovalMenu();
                                pendingRequests.add(new permissionResponseRow(entry.getKey(), dto.getNewRequestType(), barMenu2));

                            }
                        }
                    });
                } else {
                    Platform.runLater(() -> new ErrorMessage("Error: " + response.code()));
                }
            }
        });
    }

    private MenuButton createApprovalMenu() {
        MenuButton menuButton = new MenuButton("Choose");

        MenuItem approveItem = new MenuItem("APPROVE");
        MenuItem denyItem = new MenuItem("DENY");

        approveItem.setOnAction(e -> {
            menuButton.setText(APPROVED);
            menuButton.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        });
        denyItem.setOnAction(e -> {
            menuButton.setText(DENIED);
            menuButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        });

        menuButton.getItems().addAll(approveItem, denyItem);
        return menuButton;
    }


    @FXML
    public void initialize() {
        // Set up columns in the table
        userNameColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
        permissionTypeColumn.setCellValueFactory(cellData -> cellData.getValue().permissionTypeProperty());
        approvalColumn.setCellValueFactory(cellData -> cellData.getValue().approvalOptionsProperty());

        // Customize approval column to display MenuButton without anonymous Callback
        approvalColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(MenuButton item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                }
            }
        });

        // Load data into table
        permissionRequestTable.setItems(pendingRequests);
    }

    @FXML
    void clickMeCancelButton(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    @FXML
    void clickMeOKButton(ActionEvent event) {
        sendApprovalUpdates();
        ((Stage) OKButton.getScene().getWindow()).close();
    }

    // Send updated statuses to server via POST
    private void sendApprovalUpdates() {

        Map<String, DTOPermissionRequest> permissionsMap = createPermissionsMapFromTable();

        Gson gson = new Gson();
        String json = gson.toJson(permissionsMap);

        String url = PERMISSION_UPDATE + "?sheetName=" + sheetName;

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        HttpClientUtil.HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> new ErrorMessage("Failed to update permissions: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Platform.runLater(() -> new ErrorMessage("Error updating permissions: " + response.code()));
                } else {
                    Platform.runLater(() -> new GeneralMessage("Permissions updated successfully."));
                }
            }
        });

    }


    private Map<String, DTOPermissionRequest> createPermissionsMapFromTable() {
        Map<String, DTOPermissionRequest> permissionsMap = new HashMap<>();

        for (permissionResponseRow row : permissionRequestTable.getItems()) {
            String userName = row.getUserName();
            String permissionType = row.getPermissionType();
            String status =  row.getApprovalOptions().getText();

            if (status != "Choose") {
                DTOPermissionRequest dtoRequest = new DTOPermissionRequest(permissionType,userName, status,null);
                permissionsMap.put(userName, dtoRequest);
            }
        }

        return permissionsMap;
    }
}



