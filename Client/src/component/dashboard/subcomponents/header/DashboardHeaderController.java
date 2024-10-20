package component.dashboard.subcomponents.header;

import component.dashboard.main.maindashboard.DashboardController;
import component.dashboard.subcomponents.availableSheets.AvailableSheetRow;
import component.popup.error.ErrorMessage;
import dto.DTOSheetInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import util.http.HttpClientUtil;
import JsonSerializer.JsonSerializer;
import java.io.File;

import static util.Constants.LOAD;

public class DashboardHeaderController {


    private DashboardController dashboardController;

    @FXML private Button loadSheetFileButton;
    @FXML private Button ackOrDenyPermissionRequestButton;

    @FXML void loadSheetFileButtonHandler(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

            Stage stage = (Stage) loadSheetFileButton.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {

                RequestBody body =
                        new MultipartBody.Builder()
                                .addFormDataPart("file", selectedFile.getName(), RequestBody.create(selectedFile, MediaType.parse("text/plain")))
                                .build();

                Request request = new Request.Builder()
                        .url(LOAD)
                        .post(body)
                        .build();

                Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
                Response response = call.execute();

                if (response.isSuccessful()) {

// Convert the response to DTOSheetInfo
                    String jsonResponse = response.body().string();
                    JsonSerializer jsonSerializer = new JsonSerializer();
                    DTOSheetInfo sheetInfo = jsonSerializer.convertJsonToDTOSheetInfo(jsonResponse);

                    dashboardController.createAvailableSheetRow(sheetInfo);

                }else{
                    new ErrorMessage("Something went wrong: " + response.body().string());

                }


            } else {
                new ErrorMessage("No file selected");
            }
        }catch (Exception e) {
            new ErrorMessage(e.getMessage());
        }

    }




    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }


}




