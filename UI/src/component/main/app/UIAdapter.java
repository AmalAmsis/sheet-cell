package component.main.app;

import dto.DTOSheet;
import engine.Engine;
import jakarta.xml.bind.JAXBException;
import jaxb.schema.xmlprocessing.FileDataException;
import manager.UIManager;
import manager.UIManagerImpl;
import sheet.Sheet;
import component.subcomponent.header.HeaderController;
import component.subcomponent.sheet.SheetController;

import java.io.FileNotFoundException;
import engine.EngineImpl;

public class UIAdapter {

    private UIManager uiManager;
    private HeaderController headerController;
    private SheetController sheetController;

    public UIAdapter(){
        this.uiManager = new UIManagerImpl();
    };

    public UIAdapter(HeaderController headerController,SheetController sheetController) {
        this.uiManager = new UIManagerImpl();
        this.headerController = headerController;
        this.sheetController = sheetController;
    }


    // Setters for the controllers
    public void setHeaderController(HeaderController headerController) {
        this.headerController = headerController;
    }

    public void setSheetController(SheetController sheetController) {
        this.sheetController = sheetController;
    }

    public SheetController getSheetController() {
        return sheetController;
    }


    // Method to handle the loading of an XML file and updating the sheet
    public void loadSheetFromFile(String filePath) throws Exception {
        // Load the sheet from the XML file
        uiManager.loadSheetFromXmlFile(filePath);

    }


    public void displaySheet(){
        DTOSheet dtoSheet = uiManager.getDtoSheetForDisplaySheet();
        sheetController.diaplaySheet(dtoSheet);
    }


}
