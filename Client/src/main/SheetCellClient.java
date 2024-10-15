package main;

import component.main.SheetCellAppMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static util.Constants.*;

public class SheetCellClient extends Application {
    private SheetCellAppMainController sheetCellAppMainController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(300);
        primaryStage.setTitle("Sheet Cell Client");

        URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            sheetCellAppMainController = fxmlLoader.getController();

            Scene scene = new Scene(root, 800, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.shutdown();
        sheetCellAppMainController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
