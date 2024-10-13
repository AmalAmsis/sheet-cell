package component.dashboard.subcomponents.header;

import component.popup.error.ErrorMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;

import static util.Constants.BASE_URL;
import static util.Constants.LOAD;

public class DashboardHeaderController {

    @FXML private Button loadSheetFileButton;
    @FXML private Button ackOrDenyPermissionRequestButton;
    @FXML private Button requestPermissionButton;
    @FXML private Button viewSheetButton;
    @FXML private VBox availableSheetTable;



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
                    CheckBox checkBox = new CheckBox(selectedFile.getName());
                    availableSheetTable.getChildren().add(checkBox);
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

    @FXML void ackOrDenyPermissionRequestButtonHandler(ActionEvent event) {

    }

    @FXML void requestPermissionButtonHandler(ActionEvent event) {

    }

    @FXML void viewSheetButtonHandler(ActionEvent event) {

    }


}




