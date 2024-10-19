package sheetmanager;

import dto.DTOCell;
import dto.DTORange;
import dto.DTOSheet;
import sheetmanager.state.SheetStateManager;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface SheetManager {


    void loadsheetFromStream(InputStream inputStream, String fileName) throws Exception;

    /** Action #2 - display current sheet
     * @return A DTOSheet object representing the sheet.*/
    DTOSheet displaySheet();

    /** Action #3 - Displays the DTO representation of a specific cell based on its coordinate.
     * @param coordinateString The coordinate of the cell as a string (e.g., "A1").
     * @return A DTOCell object representing the cell at the specified coordinate.*/
    DTOCell displayCell(String coordinateString);

    /** Action #4 - Updates the value of a specific cell in the sheet and returns the updated sheet.
     * @param coordinateString The coordinate of the cell as a string (e.g., "A1").
     * @param newOriginalValue The new value to be set in the cell.
     * @return A DTOSheet object representing the updated sheet.
     * @throws Exception If there is an issue*/
    DTOSheet updateCell(String coordinateString, String newOriginalValue) throws Exception;

    /** Action #5 - Retrieves a DTOSheet representation of a specific version of the sheet.
     * @param versionNumber The version number of the sheet to retrieve.
     * @return A DTOSheet object representing the sheet of the specified version.*/
    DTOSheet displaySheetVersion(int versionNumber);

    SheetStateManager getCurrentSheetState();
    void addNewRange(String rangeName, String fromCoordinate, String toCoordinate) throws Exception;
    void removeRange(String rangeName)throws Exception;
    DTORange getRange(String rangeName);
    List<DTORange> getAllRanges();
    DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities) throws Exception;
    DTOSheet filterSheet(Map<String, List<String>> selectedColumnValues, String from, String to) throws Exception;
    DTOSheet updateTemporaryCellValue(String coordinateString, String newOriginalValue);
    int getSheetVersion();



    /** NOT IN WAS ANYMORE :

    /**
     * Action #0
     * Initializes an empty system with a new, empty sheet.
     */
    void initializeEmptySystem();

    /**
     * Action #1
     * Loads a sheet from a file.
     * If the file is loaded successfully, the method completes silently.
     * If there's an error, an exception is thrown.
     *
     * @param filePath The path to the file from which the sheet will be loaded.
     * @throws Exception If there is an issue loading the file.
     */
    void loadSheetFromXmlFile(String filePath) throws Exception;

    /** Action #6 - Saves the current state of the system, including the current sheet and all its versions, to a file.
     * @param filePath The path to the file where the system state will be saved.
     * @throws Exception If there is an issue saving the file.*/
    void saveSystemState(String filePath) throws Exception;

    /** Action #7 - Loads the state of the system from a file, including the current sheet and all its versions.
     * If the state is loaded successfully, the method completes silently.
     * If there's an error, an exception is thrown.
     * @param filePath The path to the file from which the system state will be loaded.
     * @throws Exception If there is an issue loading the file.*/
    void loadSystemState(String filePath) throws Exception;


}
