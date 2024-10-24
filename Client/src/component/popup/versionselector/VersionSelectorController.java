package component.popup.versionselector;

import JsonSerializer.JsonSerializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import component.popup.error.ErrorMessage;
import component.selectedSheetView.main.SelectedSheetViewController;
import dto.DTOSheet;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import component.popup.viewonlysheet.ViewOnlySheetController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import util.http.HttpClientUtil;

import static util.Constants.VERSION_CHANGES;
import static util.Constants.VERSION_SHEET;

public class VersionSelectorController {

    private SelectedSheetViewController selectedSheetViewController;
    private List<Integer> changesPerVersion = new ArrayList<>();

    @FXML private Label numOfChangesInSelectedVersionLabel;
    @FXML private Label selectedVersionLabel;
    @FXML private MenuButton versionMenuBar;
    @FXML private Button okButton;

    public void initialize() {


        try {
            fetchChangesPerVersion();
        }catch (Exception e){
            Platform.runLater(()->{
                new ErrorMessage(e.getMessage());
            });
        }

        // Disable the OK button initially
        okButton.setDisable(true);

        // Add listener to enable OK button when an item is selected
        versionMenuBar.getItems().forEach(menuItem ->
                menuItem.setOnAction(event -> {
                    versionMenuBar.setText(menuItem.getText()); // Update the MenuButton text
                    okButton.setDisable(false);  // Enable the OK button
                })
        );
    }

    public void fetchChangesPerVersion() throws IOException {
        // Assuming the VERSION_CHANGES constant is the URL to the servlet.
        String url = VERSION_CHANGES;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // Make an asynchronous request using OkHttp
        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        try {
            if (response.isSuccessful()) {

                String jsonResponse = response.body().string();

                // Deserialize the JSON response into a List of Integer values
                Gson gson = new Gson();
                List<Integer> fetchedChangesPerVersion = gson.fromJson(jsonResponse, new TypeToken<List<Integer>>() {
                }.getType());

                // Update the changesPerVersion list and UI on the JavaFX Application Thread
                changesPerVersion.clear();
                changesPerVersion.addAll(fetchedChangesPerVersion);
                loadVersionToMenuBar();
            } else {
                Platform.runLater(() -> {
                    new ErrorMessage("Server returned error code: " + response.code());
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setSelectedSheetViewController(SelectedSheetViewController selectedSheetViewController) {
        this.selectedSheetViewController = selectedSheetViewController;
    }

    @FXML
    void clickMeCancelButtom(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void clickMeOKButtom(ActionEvent event) {


        // Get the selected version from the selectedVersionLabel
        String selectedVersionText  = selectedVersionLabel.getText();
        String selectedVersion = selectedVersionText.replaceAll("[^\\d]", ""); // This removes any non-digit characters

        if (selectedVersion.isEmpty()) {
            displayErrorMessage("No version selected.");
            return;
        }


        // Build the request URL including the selected version as a query parameter
        String url = VERSION_SHEET + "?version=" + selectedVersion;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // Make an asynchronous request using OkHttp
        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                displayErrorMessage("Error in request: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    JsonSerializer jsonSerializer = new JsonSerializer();
                    DTOSheet versionSheet = jsonSerializer.convertJsonToDto(jsonResponse);
                    loadVersion(versionSheet);
                } else {
                    displayErrorMessage("Failed to fetch filtered sheet: " + response.code());
                }
                //return null;
            }
        });

    }

    private void displayErrorMessage(String message) {
        Platform.runLater(() -> {
            new ErrorMessage(message);
        });
    }


    public void loadVersion(DTOSheet dtoVersionSheet) {

        Platform.runLater(() -> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/popup/viewonlysheet/viewOnlySheet.fxml"));
                Parent root = loader.load();

                ViewOnlySheetController viewOnlySheetController = loader.getController();
                viewOnlySheetController.setAppController(selectedSheetViewController);
                viewOnlySheetController.displayViewOnlySheetByVersion(dtoVersionSheet);

                int versionNumber = dtoVersionSheet.getSheetVersion();

                Stage stage = new Stage();
                stage.setTitle("Version " + versionNumber);
                stage.setScene(new Scene(root));
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }



    public void loadVersionToMenuBar() {

        int numOfVersion = getNumOfVersions();
        versionMenuBar.getItems().clear();
        for (int i = 1; i < numOfVersion; i++) {

            MenuItem versionItem = new MenuItem("Version " + i);

            int version = i;
            versionItem.setOnAction(e -> {setVersionData(version);});

            // Add an action listener to each menu item when it's added
            versionItem.setOnAction(e -> {
                setVersionData(version);
                okButton.setDisable(false); // Enable the OK button when a version is selected
            });

            versionMenuBar.getItems().add(versionItem);
        }
    }

    private int getNumOfVersions() {
        return changesPerVersion.size();
    }

    public void setVersionData(int version){
        int numOfChanges = getNumOfChangesInVersion(version);
        numOfChangesInSelectedVersionLabel.setText(String.valueOf(numOfChanges));
        selectedVersionLabel.setText(String.valueOf(version));
        numOfChangesInSelectedVersionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1abc9c; -fx-font-size: 14px;");
        selectedVersionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1abc9c; -fx-font-size: 14px;");
    }

    private int getNumOfChangesInVersion(int version) {
        return changesPerVersion.get(version);
    }


}
