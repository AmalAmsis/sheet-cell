package component.dashboard.main.maindashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestLoadFile extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // טוען את ה-FXML עם הנתיב המדויק
            Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
            primaryStage.setTitle("Header Page");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
