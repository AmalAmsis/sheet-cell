package component.dashboard.subcomponents.command;

import JsonSerializer.JsonSerializer;
import component.dashboard.main.maindashboard.DashboardController;
import component.popup.error.ErrorMessage;
import component.popup.message.GeneralMessage;
import component.popup.permissionRequest.PermissionRequestController;
import component.popup.permissionResponse.PermissionResponseController;
import dto.DTOCell;
import dto.DTOSheet;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import util.http.HttpClientUtil;



import java.io.IOException;
import java.util.Map;

import javafx.event.ActionEvent;

import static util.Constants.USER_PERMISSION;
import static util.Constants.VIEW;

public class CommandController {

    private DashboardController dashboardController;

    @FXML private Button ackOrDenyPermissionRequestButton;
    @FXML private Button requestPermissionButton;
    @FXML private Button viewSheetButton;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }


    public DashboardController getDashboardController() {
        return dashboardController;
    }


    @FXML void viewSheetHandler(ActionEvent event) {

        String fileName = dashboardController.getSelectedSheetName();

        if(fileName != null) {
            String url = VIEW + "?sheetName=" + fileName;

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

                    String permission = getUserPermission(fileName);
                    dashboardController.switchToSelectedSheetView(dtoSheet,fileName,permission);
                } else {
                    // Display server error message if available
                    String errorMessage = response.body() != null ? response.body().string() : "Unknown server error";
                    new ErrorMessage("Failed to fetch sheet: " + errorMessage);                }

            } catch (IOException e) {
                new ErrorMessage("Error fetching sheet: " + e.getMessage());
            }
        } else {
            new GeneralMessage("No sheet selected.\n Please select from the available sheets.");
        }
    }

    @FXML void requestPermissionHandler(ActionEvent event) {

        Platform.runLater(() -> {

            // Get selected sheet name from the DashboardController
            String sheetName = dashboardController.getSelectedSheetName();

            // Check if a sheet is selected
            if (sheetName == null) {
                new ErrorMessage("Please select a sheet to request permission.");
                return;
            }

            // Check if the user is the owner of the sheet
            String uploadedBy = dashboardController.getUploaderName(sheetName);
            String currentUsername = dashboardController.getCurrentUsername();

            if (uploadedBy.equals(currentUsername)) {
                new ErrorMessage("You are the owner of this sheet and already have all permissions.");
                return;
            }

            try {
                // Load the FXML for the permission request popup
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/popup/permissionRequest/permissionRequest.fxml"));
                Parent root = loader.load();

                // Get the controller for the permission request popup
                PermissionRequestController permissionRequestController = loader.getController();

                // Initialize the popup with the selected sheet name
                permissionRequestController.initializePopup(sheetName);

                // Show the popup in a new window
                Stage stage = new Stage();
                stage.setTitle("Permission Request");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                new ErrorMessage("Failed to open permission request window:\n " + e.getMessage());
            }
        });
    }


    @FXML void responsePermissionHandler(ActionEvent event) {
        Platform.runLater(() -> {

            // Get selected sheet name from the DashboardController
            String sheetName = dashboardController.getSelectedSheetName();

            // Check if a sheet is selected
            if (sheetName == null) {
                new GeneralMessage("Please select a sheet to request permission.");
                return;
            }

            // Check if the user is the owner of the sheet
            String uploadedBy = dashboardController.getUploaderName(sheetName);
            String currentUsername = dashboardController.getCurrentUsername();

            if (uploadedBy.equals(currentUsername)) {
                try {
                    // Load FXML for permission response popup
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/popup/permissionResponse/permissionResponse.fxml"));
                    Parent root = loader.load();

                    // Get controller for the permission response popup
                    PermissionResponseController permissionResponseController = loader.getController();

                    // Initialize the popup with the selected sheet name
                    permissionResponseController.initializePopup(sheetName);

                    // Show the popup in a new window
                    Stage stage = new Stage();
                    stage.setTitle("Manage Permission Requests");
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException e) {
                    new ErrorMessage("Failed to open permission response window: " + e.getMessage());
                }

            }
            else{
                new GeneralMessage("You are not the owner of this sheet.\nYou are not allowed to approve or deny permissions.");
            }

        });
    }


    String getUserPermission(String sheetName) {
        String url = USER_PERMISSION + "?sheetName=" + sheetName;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        try (Response response = call.execute()) {
            if (response.isSuccessful()) {
                return response.body().string(); // Permission level as returned by the server
            } else {
                System.out.println("Failed to retrieve user permission: " + response.code());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error retrieving user permission: " + e.getMessage());
            return null;
        }
    }

}
