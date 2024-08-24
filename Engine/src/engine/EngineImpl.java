package engine;

import dto.DTOCell;
import dto.DTOCellImpl;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import jaxb.schema.generated.STLSheet;
import jaxb.schema.xmlprocessing.XmlProcessing;
import jaxb.schema.xmlprocessing.XmlProcessingImpl;
import sheet.Sheet;
import sheet.SheetImpl;
import sheet.coordinate.Coordinate;
import sheet.version.SheetVersionHandler;
import sheet.version.SheetVersionHandlerImpl;
import state.SheetStateManager;
import state.SheetStateManagerImpl;

public class EngineImpl implements Engine {

    SheetStateManager currentSheetState;

    @Override
    public void initializeEmptySystem() {
        this.currentSheetState = null;
    }

    @Override
    public void loadSheetFromXmlFile(String filePath) throws Exception {

        //load the xml file with jaxb
        XmlProcessing xmlProcessing = new XmlProcessingImpl();
        STLSheet stlSheet = xmlProcessing.parseAndValidateXml(filePath);

        //Loading system state
        Sheet newSheet = new SheetImpl(stlSheet);
        SheetVersionHandler currentSheetVersionHandler = new SheetVersionHandlerImpl(newSheet);
        this.currentSheetState = new SheetStateManagerImpl(newSheet,currentSheetVersionHandler);
    }

    @Override
    public DTOSheet displaySheet() {
        if (this.currentSheetState != null) {
            Sheet mySheet = this.currentSheetState.getCurrentSheet();
            return new DTOSheetImpl(mySheet);
        }
        return null;
    }

    @Override
    public DTOCell displayCell(String coordinateString) {
        if (this.currentSheetState != null){
            Sheet mySheet = this.currentSheetState.getCurrentSheet();
            Coordinate myCoordinate = mySheet.convertStringToCoordinate(coordinateString);
            //If it does not trow exception we are the String is ok.
            return new DTOCellImpl(mySheet.getCell(myCoordinate));
        }
        return null;
    }

    @Override
    public DTOSheet updateCell(String coordinateString, String newOriginalValue) throws Exception {
        if (this.currentSheetState != null){
            //call Amal function

            //update currentSheetState
            //return new DTOSheetImpl(mySheet);
        }
        return null;
    }

    @Override
    public DTOSheet displaySheetVersion(int versionNumber) {
        if (this.currentSheetState != null){
            SheetVersionHandler sheetVersionHandler = this.currentSheetState.getVersionHandler();
            Sheet mySheet = sheetVersionHandler.getSheetByVersion(versionNumber);
            return new DTOSheetImpl(mySheet);
        }
        return null;
    }

    @Override
    public void saveSystemState(String filePath) throws Exception {

    }

    @Override
    public void loadSystemState(String filePath) throws Exception {

    }
}
