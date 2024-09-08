package component.main.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import component.subcomponent.sheet.SheetController;
import component.subcomponent.header.HeaderController;

import java.io.IOException;

public class AppController {

    //Data Member
    private UIAdapter uiAdapter;

    //FXML Member
    @FXML
    private BorderPane rootPane;

    public AppController() {
        uiAdapter = new UIAdapter();
    }

    @FXML
    public void initialize() {
        try {

            // Load and set HeaderController
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/component/subcomponent/header/header.fxml"));
            rootPane.setTop(headerLoader.load());
            HeaderController headerController = headerLoader.getController();
            uiAdapter.setHeaderController(headerController);
            headerController.setUIAdapter(uiAdapter);


            // Load and set SheetController
            FXMLLoader sheetLoader = new FXMLLoader(getClass().getResource("/component/subcomponent/sheet/sheet.fxml"));
            rootPane.setCenter(sheetLoader.load());
            SheetController sheetController = sheetLoader.getController();
            uiAdapter.setSheetController(sheetController);
            sheetController.setUiAdapter(uiAdapter);

            headerController.initialize();
            sheetController.initialize();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}