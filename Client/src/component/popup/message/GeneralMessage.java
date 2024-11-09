package component.popup.message;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GeneralMessage {

    public GeneralMessage(String message) {
        this("Note", message); // Default title to "Note"
    }

    public GeneralMessage(String title, String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        // Main message text
        Text messageText = new Text(message);
        messageText.setWrappingWidth(350);

        // OK button at the bottom
        Button okButton = new Button("OK");
        okButton.setPrefWidth(80);
        okButton.setOnAction(e -> stage.close());

        // Layout for message content
        VBox contentBox = new VBox(10, messageText);
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.CENTER_LEFT);

        // Layout for OK button
        HBox buttonBox = new HBox(okButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.setPadding(new Insets(10));

        // Combine all parts
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(contentBox);
        borderPane.setBottom(buttonBox);
        borderPane.setPadding(new Insets(10));

        // Configure scene and stage
        Scene scene = new Scene(borderPane, 400, 150);
        stage.setScene(scene);
        stage.showAndWait();
    }


}
