package component.main.app;

import component.subcomponent.header.HeaderController;
import component.subcomponent.sheet.SheetController;
import component.subcomponent.left.LeftController;


import dto.DTOSheet;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import manager.UIManager;
import manager.UIManagerImpl;


public class AppController {

    @FXML private VBox header;
    @FXML private HeaderController headerController;
    @FXML private ScrollPane sheet;
    @FXML private SheetController sheetController;
    @FXML private ScrollPane left;
    @FXML private LeftController leftController;

    private UIManager uiManager;

    public AppController(){
        this.uiManager=new UIManagerImpl();
    }


    public void loadAndDisplaySheetFromXmlFile(String filePath) throws Exception {
        loadSheetFromFile(filePath);
        displaySheet();
    }


    public void loadSheetFromFile(String filePath) throws Exception {
        // Load the sheet from the XML file
        uiManager.loadSheetFromXmlFile(filePath);

    }

    public void displaySheet(){
        DTOSheet dtoSheet = uiManager.getDtoSheetForDisplaySheet();
        sheetController.initSheetAndBindToUIModel(dtoSheet);
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
