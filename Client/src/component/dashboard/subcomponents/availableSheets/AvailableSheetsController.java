package component.dashboard.subcomponents.availableSheets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import component.dashboard.main.maindashboard.DashboardController;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import java.util.TimerTask;
import java.util.Timer;

import static util.Constants.*;

public class AvailableSheetsController {

    private DashboardController dashboardController;
    private Timer refreshTimer;
    private TimerTask listRefresher;


    @FXML private VBox availableSheetTable;
    @FXML private VBox permissionsTable;


    public void initialize()   {
        // Load available sheets from server when initializing
        loadAvailableSheetsFromServer();
        availableSheetRefresher(); // Start the sheet refresher

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


    public void availableSheetRefresher() {
        refreshTimer = new Timer(true); // Create a daemon timer
        AvailableSheetsRefresher refresher = new AvailableSheetsRefresher(this); // Pass the controller instance
        refreshTimer.schedule(refresher, REFRESH_RATE, REFRESH_RATE); // Run every 2 seconds
    }

    public void refreshAvailableSheets(List<String> newSheets) {

        // Get the currently available sheets from the table
        List<String> currentSheets = availableSheetTable.getChildren().stream()
                .filter(child -> child instanceof CheckBox)
                .map(child -> ((CheckBox) child).getText())
                .toList();

        // Add only sheets that are not already in the table
        Platform.runLater(() -> {
            for (String fileName : newSheets) {
                if (!currentSheets.contains(fileName)) {
                    CheckBox checkBox = new CheckBox(fileName);
                    addSheetToAvailableSheetTable(checkBox);
                }
            }
        });
    }




}
