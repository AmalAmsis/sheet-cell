package component.dashboard.subcomponents.header;

import component.popup.error.ErrorMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;

import static util.Constants.LOAD;


public class DashboardHeaderController {

    @FXML
    private Button loadSheetFileButton;

    @FXML
    void handleLoadSheetFile(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

            Stage stage = (Stage) loadSheetFileButton.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {

                String RESOURCE = "/upload-file";

                RequestBody body =
                        new MultipartBody.Builder()
                                .addFormDataPart("file", selectedFile.getName(), RequestBody.create(selectedFile, MediaType.parse("application/xml")))
                                .build();

                Request request = new Request.Builder()
                        .url( LOAD)
                        .post(body)
                        .build();

                Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() != 200) {
                            String responseBody = response.body().string();
                            Platform.runLater(() ->
                                    new ErrorMessage("Something went wrong: " + responseBody)
                            );
                        } else {
                            Platform.runLater(() -> {
                                //had the file.
                                new ErrorMessage("yessssssssss");
                            });
                        }
                    }

                    @Override public void onFailure(@NotNull Call call, IOException e) {
                        Platform.runLater(() -> new ErrorMessage("Failed to upload file: " + e.getMessage()));
                    }
                });


                // loadFileWithProgress(selectedFile.getAbsolutePath());
            } else {
                new ErrorMessage("No file selected");
            }
            }catch (Exception e) {
                new ErrorMessage(e.getMessage());
            }

        }
    }




