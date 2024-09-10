package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FxmlTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // טעינת הקובץ הראשי app.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../component/main/app/app.fxml"));
            BorderPane root = loader.load();

            // הגדרת הסצנה והבמה (Stage)
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Test JavaFX App");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

