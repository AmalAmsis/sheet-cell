package subcomponents.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import sheet.SheetImpl;
import subcomponents.sheet.SheetController;
import subcomponents.header.HeaderController;

import java.io.IOException;
import java.util.Objects;

public class AppController {

    @FXML
    private BorderPane rootPane;

    private HeaderController headerController;
    private SheetController sheetController;


    @FXML
    public void initialize() {
        try {
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/subcomponents/header/header.fxml"));
            Parent headerRoot = headerLoader.load();
            headerController = headerLoader.getController();
            rootPane.setTop(headerRoot);

            FXMLLoader sheetLoader = new FXMLLoader(getClass().getResource("/subcomponents/sheet/sheet.fxml"));
            Parent sheetRoot = sheetLoader.load();
            sheetController = sheetLoader.getController();
            rootPane.setCenter(sheetRoot);

            headerController.initialize();
            sheetController.initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Method to load the sheet into the SheetController
    public void loadSheet(SheetImpl sheet) {
        // Pass the sheet object to the SheetController
        sheetController.loadSheet(sheet);
    }

}