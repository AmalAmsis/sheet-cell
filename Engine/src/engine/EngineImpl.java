package engine;

import dto.DTOCell;
import dto.DTOCellImpl;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import jakarta.xml.bind.JAXBException;
import jaxb.schema.generated.*;
import jaxb.schema.xmlprocessing.FileDataException;
import jaxb.schema.xmlprocessing.XmlProcessing;
import jaxb.schema.xmlprocessing.XmlProcessingImpl;
import sheet.Sheet;
import sheet.SheetImpl;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;
import sheet.range.RangeManager;
import sheet.range.RangeManagerImpl;
import sheet.version.SheetVersionHandler;
import sheet.version.SheetVersionHandlerImpl;
import state.SheetStateManager;
import state.SheetStateManagerImpl;

import java.io.*;
import java.util.List;

public class EngineImpl implements Engine,Serializable {

    private SheetStateManager currentSheetState;

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
        char lastLetterOfCol = (char)(stlSheet.getSTLLayout().getColumns() +'A' -1);
        int latsNumOfRows = stlSheet.getSTLLayout().getRows();

        RangeManager rangeManager = new RangeManagerImpl();
        if(stlSheet.getSTLRanges() != null) {
            for (STLRange stlRange: stlSheet.getSTLRanges().getSTLRange()){
                Coordinate coordinateFrom = convertStringToCoordinate(stlRange.getSTLBoundaries().getFrom(),lastLetterOfCol,latsNumOfRows);
                Coordinate coordinateTo = convertStringToCoordinate(stlRange.getSTLBoundaries().getTo(),lastLetterOfCol,latsNumOfRows);
                rangeManager.addRange(stlRange.getName(),coordinateFrom,coordinateTo);
            }
        }

        List<STLCell> sortedListOfStlCells = xmlProcessing.getTopologicalSortOrThrowCircularReferenceException(stlCellList, rangeManager);
        Sheet newSheet = new SheetImpl(stlSheet, sortedListOfStlCells,rangeManager);
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


    public Coordinate convertStringToCoordinate(String stringCoordinate,char lastLatterOfCol, int lastNumOfrow) {
        // Check if the input is null or of incorrect length
        if (stringCoordinate == null || stringCoordinate.length() < 2 || stringCoordinate.length() > 3) {
            throw new IllegalArgumentException("Input must be between 2 to 3 characters long and non-null.");
        }


        // get the col letter and checking that a letter representing the column is in the col range of the sheet
        char col = stringCoordinate.toUpperCase().charAt(0);
        if (col < 'A' || col > lastLatterOfCol) {
            throw new IllegalArgumentException("Column must be a letter between A and " + lastLatterOfCol + ".");
        }

        //the follow must be a number
        for (int i = 1; i < stringCoordinate.length(); i++) {
            if (!Character.isDigit(stringCoordinate.charAt(i))) {
                throw new IllegalArgumentException("The input format is invalid. It should be a letter followed by digits.");
            }
        }

        // get row number
        int row;
        try {
            row = Integer.parseInt(stringCoordinate.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Row must be a valid number.");
        }

        // check if is in the row range
        if (row < 1 || row > lastNumOfrow) {
            throw new IllegalArgumentException("Row must be between 1 and " + lastNumOfrow + ".");
        }

        // create the coordinate
        return new CoordinateImpl(col, row);
    }
}
