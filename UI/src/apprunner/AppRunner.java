package apprunner;

import component.main.app.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppRunner extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // טוען את התצוגה הראשית של המערכת
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/main/app/app.fxml"));
            Parent root = loader.load();

            // קבלת ה-AppController והגדרת ה-Stage הראשי
            AppController appController = loader.getController();
            appController.setPrimaryStage(primaryStage);

            Scene scene = new Scene(root, 900, 680);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Main Application");

            // הטענת ערכת צבעים דיפולטית
            appController.applyTheme("Style 1");

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
