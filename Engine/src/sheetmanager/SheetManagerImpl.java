package sheetmanager;

import dto.*;
import jakarta.xml.bind.JAXBException;
import uploadfilemanager.jaxb.generated.STLCell;
import uploadfilemanager.jaxb.generated.STLCells;
import uploadfilemanager.jaxb.generated.STLRange;
import uploadfilemanager.jaxb.generated.STLSheet;
import uploadfilemanager.jaxb.xmlprocessing.FileDataException;
import uploadfilemanager.jaxb.xmlprocessing.XmlProcessing;
import uploadfilemanager.jaxb.xmlprocessing.XmlProcessingImpl;
import uploadfilemanager.LoadFile;
import sheetmanager.sheet.Sheet;
import sheetmanager.sheet.SheetImpl;
import sheetmanager.sheet.command.filtersheet.FilterSheet;
import sheetmanager.sheet.command.filtersheet.FilterSheetImpl;
import sheetmanager.sheet.coordinate.Coordinate;
import sheetmanager.sheet.coordinate.CoordinateImpl;
import sheetmanager.sheet.range.Range;
import sheetmanager.sheet.range.RangeManager;
import sheetmanager.sheet.range.RangeManagerImpl;
import sheetmanager.sheet.range.RangeReadActions;
import sheetmanager.sheet.command.filtersortdatapreparation.ValidationException;
import sheetmanager.sheet.command.sortsheet.SortSheet;
import sheetmanager.sheet.command.sortsheet.SortSheetImpl;
import sheetmanager.sheet.version.SheetVersionHandler;
import sheetmanager.sheet.version.SheetVersionHandlerImpl;
import sheetmanager.state.SheetStateManager;
import sheetmanager.state.SheetStateManagerImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SheetManagerImpl implements SheetManager,Serializable {

    private SheetStateManager currentSheetState;
    public SheetStateManager getCurrentSheetState() {
        return currentSheetState;
    }

    @Override
    public void addNewRange(String rangeName, String fromCoordinate, String toCoordinate) throws Exception
    {
        String range = fromCoordinate + ".." + toCoordinate;
        currentSheetState.getCurrentSheet().addRangeToManager(rangeName,range);
    }

    @Override
    public void removeRange(String rangeName) throws Exception{
        currentSheetState.getCurrentSheet().removeRangeFromManager(rangeName);

    }

    @Override
    public DTORange getRange(String rangeName) {
        RangeReadActions range = currentSheetState.getCurrentSheet().getRangeReadActions(rangeName);
        return new DTORangeImpl(range);
    }

    @Override
    public List<DTORange> getAllRanges() {
        List<DTORange> ranges = new ArrayList<DTORange>();
        Map<String,Range> rangesMap = currentSheetState.getCurrentSheet().getRangeManager().getRanges();
        for (Range range : rangesMap.values()) {
            ranges.add(new DTORangeImpl(range));
        }
        return ranges;
    }

    @Override
    public DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities)throws Exception{

        SortSheet sortSheet = new SortSheetImpl(currentSheetState.getCurrentSheet());
        try{
             return  sortSheet.getSortedSheet( from,  to,  listOfColumnsPriorities);
        }catch ( ValidationException e){
            throw new Exception(e.getMessage());
        }

    }

    @Override public DTOSheet filterSheet(Map<String, List<String>> selectedColumnValues, String from, String to) throws Exception{
        FilterSheet filterSheet = new FilterSheetImpl(currentSheetState.getCurrentSheet());
        try{
            return  filterSheet.filterSheet(selectedColumnValues, from, to);
        }catch ( ValidationException e){
            throw new Exception(e.getMessage());
        }
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
            Coordinate coordinate = this.currentSheetState.getCurrentSheet().convertStringToCoordinate(coordinateString.replace(":",""));
            //yarden 27/8
            int numOfUpdatededCells = this.currentSheetState.getCurrentSheet().setCell(coordinate, newOriginalValue);// הורדתי מהערה
            Sheet mySheet = this.currentSheetState.getCurrentSheet();
            this.currentSheetState.getVersionHandler().addNewVersion(mySheet,numOfUpdatededCells);//ורדתי מהערה
            return new DTOSheetImpl(mySheet);
        }
        return null;
    }

    public DTOSheet updateTemporaryCellValue(String coordinateString, String newOriginalValue) {
        if (this.currentSheetState != null) {
            // Create a deep copy of the current sheet
            Sheet myOriginalSheet = this.currentSheetState.getCurrentSheet();
            Sheet mySheetCopy = myOriginalSheet.createDeepCopy(); // Assuming you have a deep copy method

            // Convert coordinate and update the copied sheet
            Coordinate coordinate = mySheetCopy.convertStringToCoordinate(coordinateString.replace(":", ""));
            int numOfUpdatedCells = mySheetCopy.setCell(coordinate, newOriginalValue);

            // Return the DTO for the updated copy
            return new DTOSheetImpl(mySheetCopy);
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


    //******************************************************************************************************************************//
    @Override
    public void loadsheetFromStream(InputStream inputStream, String fileName) throws Exception{
        LoadFile loadFile = new LoadFile();
        XmlProcessing xmlProcessing = new XmlProcessingImpl();
        STLSheet stlSheet = loadFile.parseAndValidatefile(inputStream, fileName);


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

}
