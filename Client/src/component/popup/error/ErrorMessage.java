package component.popup.error;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ErrorMessage {

    Alert alert = new Alert(Alert.AlertType.ERROR);


    public ErrorMessage (String message) {
        alert.setTitle("Error"); // כותרת החלון
        alert.setHeaderText(null);

        Text text = new Text(message);
        text.setWrappingWidth(400);  // רוחב מועדף

        VBox content = new VBox();
        content.getChildren().add(text);

        alert.getDialogPane().setContent(content);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);

        alert.showAndWait();

    }
}
