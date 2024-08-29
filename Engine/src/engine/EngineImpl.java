package engine;

import dto.DTOCell;
import dto.DTOCellImpl;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import jakarta.xml.bind.JAXBException;
import jaxb.schema.generated.STLCell;
import jaxb.schema.generated.STLCells;
import jaxb.schema.generated.STLSheet;
import jaxb.schema.xmlprocessing.FileDataException;
import jaxb.schema.xmlprocessing.XmlProcessing;
import jaxb.schema.xmlprocessing.XmlProcessingImpl;
import sheet.Sheet;
import sheet.SheetImpl;
import sheet.coordinate.Coordinate;
import sheet.version.SheetVersionHandler;
import sheet.version.SheetVersionHandlerImpl;
import state.SheetStateManager;
import state.SheetStateManagerImpl;

import java.io.*;
import java.util.List;

public class EngineImpl implements Engine,Serializable {

    SheetStateManager currentSheetState;

    public SheetStateManager getCurrentSheetState() {
        return currentSheetState;
    }


    @Override
    public void initializeEmptySystem() {
        this.currentSheetState = null;
    }

    @Override
    public void loadSheetFromXmlFile(String filePath) throws FileDataException, JAXBException, FileNotFoundException {

        //load the xml file with jaxb
        XmlProcessing xmlProcessing = new XmlProcessingImpl();
        STLSheet stlSheet = xmlProcessing.parseAndValidateXml(filePath);

        //Creating a list of cells by topological sorting.
        //Returns a sorted list or exception
        STLCells stlCellList = stlSheet.getSTLCells();
        List<STLCell> sortedListOfStlCells = xmlProcessing.getTopologicalSortOrThrowCircularReferenceException(stlCellList);


         Sheet newSheet = new SheetImpl(stlSheet, sortedListOfStlCells);
         int numOfCellsInTheSheet = newSheet.getBoard().size();

        //********************************************************************************************************************//
         SheetVersionHandler currentSheetVersionHandler = new SheetVersionHandlerImpl(newSheet,numOfCellsInTheSheet);
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

    //yarden 27/8
    @Override
    public DTOSheet updateCell(String coordinateString, String newOriginalValue) throws Exception {
        if (this.currentSheetState != null){
            //call Amal function לברר אם הבנתי נכון את הסטייט
            Coordinate coordinate = this.currentSheetState.getCurrentSheet().convertStringToCoordinate(coordinateString);
            //yarden 27/8
            int numOfUpdatededCells = this.currentSheetState.getCurrentSheet().setCell(coordinate, newOriginalValue);// הורדתי מהערה
            Sheet mySheet = this.currentSheetState.getCurrentSheet();
            this.currentSheetState.getVersionHandler().addNewVersion(mySheet,numOfUpdatededCells);//ורדתי מהערה
            return new DTOSheetImpl(mySheet);
        }
        return null;
    }

    @Override
    public DTOSheet displaySheetVersion(int versionNumber) {
        if (this.currentSheetState != null){
            SheetVersionHandler sheetVersionHandler = this.currentSheetState.getVersionHandler();
            return sheetVersionHandler.getSheetByVersion(versionNumber);
        }
        return null;
    }

    @Override
    public void saveSystemState(String filePath) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            // Serialize the current state of the sheet
            out.writeObject(currentSheetState);
            //ui massage: System.out.println("System state saved successfully to " + filePath);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("The specified file path is invalid or the directory does not exist. \nPlease check the path and try again.");
        } catch (IOException e) {
            throw new IOException("An issue occurred while saving the system state. \nPlease ensure that you have write permissions and enough disk space.");
        }
    }

    @Override
    public void loadSystemState(String filePath) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            // Deserialize the system state from the file
            currentSheetState = (SheetStateManagerImpl) in.readObject();
            //ui massage: System.out.println("System state loaded successfully from " + filePath);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("The specified file was not found. \nPlease check the file path and ensure that the file exists.");
        } catch (IOException e) {
            throw new IOException("Error: An issue occurred while reading the file. \nPlease ensure that the file is accessible and not corrupted.");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Error: The file contains data that is incompatible with the current system version. \nPlease verify the file.");
        }
    }
}
