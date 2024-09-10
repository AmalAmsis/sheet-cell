package HADASH.component.main.app;

import HADASH.component.subcomponent.header.HeaderController;
import HADASH.component.subcomponent.left.LeftController;
import HADASH.component.subcomponent.sheet.SheetController;
import dto.DTOSheet;
import javafx.fxml.FXML;
import manager.UIManager;

import java.awt.*;

public class AppController {

    @FXML private ScrollPane headerComponet;
    @FXML private HeaderController headerController;
    @FXML private ScrollPane sheetComponet;
    @FXML private SheetController sheetController;
    @FXML private ScrollPane leftComponet;
    @FXML private LeftController leftController;

    private UIManager uiManager;

    public void loadSheetFromFile(String filePath) throws Exception {
        // Load the sheet from the XML file
        uiManager.loadSheetFromXmlFile(filePath);

    }

    public void displaySheet(){
        DTOSheet dtoSheet = uiManager.getDtoSheetForDisplaySheet();
        sheetController.diaplaySheet(dtoSheet);
    }


    @FXML
    public void initialize() {
       if(headerController != null && sheetController != null && leftController != null) {
            headerController.setAppController(this);
            sheetController.setAppController(this);
            leftController.setAppController(this);
        }

    }

    public void setHeaderController(HeaderController headerController) {
        this.headerController = headerController;
        headerController.setAppController(this);
    }

    public void setSheetController(SheetController sheetController) {
        this.sheetController = sheetController;
        sheetController.setAppController(this);
    }

    public void setLeftController(LeftController leftController) {
        this.leftController = leftController;
        leftController.setAppController(this);
    }

}
