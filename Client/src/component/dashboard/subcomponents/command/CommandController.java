package component.dashboard.subcomponents.command;

import JsonSerializer.JsonSerializer;
import component.dashboard.main.maindashboard.DashboardController;
import component.popup.error.ErrorMessage;
import dto.DTOSheet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import util.http.HttpClientUtil;



import java.io.IOException;
import javafx.event.ActionEvent;

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
        String username = "lo_user";  // שם המשתמש הקבוע

        if(fileName != null) {
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

                    dashboardController.switchToSelectedSheetView(dtoSheet);

                } else {
                    new ErrorMessage("Failed to fetch sheet: " + response.code());
                }

            } catch (IOException e) {
                new ErrorMessage("Error fetching sheet: " + e.getMessage());
            }
        } else {
            new ErrorMessage("No sheet selected.");
        }  }


}
