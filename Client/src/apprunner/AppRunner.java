package apprunner;

import component.main.SheetCellAppMainController;
import component.main.app.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static util.Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION;

public class AppRunner extends Application {

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
            sheetCellAppMainController.setPrimaryStage(primaryStage);

            Scene scene = new Scene(root, 800, 500);
            primaryStage.setScene(scene);
            primaryStage.show();

            // הטענת ערכת צבעים דיפולטית
            sheetCellAppMainController.applyTheme("Style 1");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.shutdown();
        if (sheetCellAppMainController != null) {
            sheetCellAppMainController.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
