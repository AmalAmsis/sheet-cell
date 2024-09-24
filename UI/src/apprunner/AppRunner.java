package apprunner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class AppRunner extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/main/app/app.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, 850, 680);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Test JavaFX App");
           // primaryStage.getScene().getStylesheets().add(getClass().getResource("/UI/src/component/subcomponent/header/header.css").toExternalForm());
            //primaryStage.getScene().getStylesheets().add(getClass().getResource("/UI/src/component/subcomponent/left/left.css").toExternalForm());
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
