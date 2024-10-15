package component.dashboard.subcomponents.availablesheets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import component.dashboard.main.maindashboard.DashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;

import static util.Constants.LOAD;

public class AvailableSheetsController {

    private DashboardController dashboardController;

    @FXML private VBox availableSheetTable;
    @FXML private VBox permissionsTable;


    public void initialize()   {
        // Load available sheets from server when initializing
        loadAvailableSheetsFromServer();
    }

    // Load available sheets from the server
    private void loadAvailableSheetsFromServer()  {
        String url = LOAD; // Replace with your actual URL

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
                    Gson gson = new Gson();
                    List<String> fileNames = gson.fromJson(jsonResponse, new TypeToken<List<String>>() {
                    }.getType());
                    for (String fileName : fileNames) {
                        CheckBox checkBox = new CheckBox(fileName);
                        addSheetToAvailableSheetTable(checkBox);
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

    public void addSheetToAvailableSheetTable(CheckBox checkBox) {
        availableSheetTable.getChildren().add(checkBox);

        checkBox.setOnAction(e -> {
            if (checkBox.isSelected()) {
                for (var child : availableSheetTable.getChildren()) {
                    if (child instanceof CheckBox && child != checkBox) {
                        ((CheckBox) child).setSelected(false);
                    }
                }
            }
        });
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }

    public String getSelectedSheetName() {
        for (var child : availableSheetTable.getChildren()) {
            if (child instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) child;
                if (checkBox.isSelected()) {
                    return checkBox.getText();
                }
            }
        }
        return null;
    }
}
