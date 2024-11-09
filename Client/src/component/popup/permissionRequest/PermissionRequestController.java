package component.popup.permissionRequest;

import component.popup.error.ErrorMessage;
import component.popup.message.GeneralMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import okhttp3.*;
import util.http.HttpClientUtil;

import javafx.event.ActionEvent;
import java.io.IOException;

import static util.Constants.SHEET_PERMISSION;

public class PermissionRequestController {


    @FXML private Button cancelButton;
    @FXML private MenuButton menuBar;
    @FXML private Button sendButton;
    @FXML private Label sheetNameLabel;
    private String selectedPermissionType;
    private String sheetName;


    private static final String PENDING = "PENDING";


    //Initiated initialization
    public void initializePopup(String sheetName) {
        this.sheetName = sheetName;
        sheetNameLabel.setText("Sheet Name: " + sheetName);
    }

    @FXML public void initialize() {
        for (MenuItem item : menuBar.getItems()) {
            item.setOnAction(e -> handlePermissionTypeSelection(item));
        }

    }

    private void handlePermissionTypeSelection(MenuItem selectedItem) {
        selectedPermissionType = selectedItem.getText();
        menuBar.setText("Permission Type: " + selectedPermissionType);
    }


    @FXML void clickMeCancelButton(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    @FXML void clickMeSendButton(ActionEvent event) {
        if (selectedPermissionType == null) {
            new ErrorMessage("Please select a permission type.");
            return;
        }

        sendPermissionRequest();

        ((Stage) cancelButton.getScene().getWindow()).close();

    }

    private void sendPermissionRequest() {
        String url = SHEET_PERMISSION;

        RequestBody formBody = new FormBody.Builder()
                .add("sheetName", sheetName)
                .add("type", selectedPermissionType)
                .add("status", PENDING)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    new ErrorMessage(e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if (response.isSuccessful()) {
                            new GeneralMessage("Permission request sent successfully.");
                        } else if (response.code() == 409) { // סטטוס קונפליקט
                            String errorMessage = response.body().string();
                            new ErrorMessage(errorMessage);
                        } else {
                            new ErrorMessage("Unexpected error: " + response.code());
                        }
                    } catch (IOException e) {
                        new ErrorMessage("Error reading response: " + e.getMessage());
                    } finally {
                        response.close();
                    }
                });
            }
        });
    }


}
