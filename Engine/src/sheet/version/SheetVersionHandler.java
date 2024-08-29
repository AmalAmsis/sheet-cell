package sheet.version;

import sheet.Sheet;
import java.util.List;


/**
 * SheetVersionHandler manages versioning for a spreadsheet.
 * This interface provides methods to add new versions, clean version history,
 * retrieve the number of versions, and access the version history.
 * It extends the SheetVersionProvider interface, inheriting its version retrieval methods.*/
public interface SheetVersionHandler  extends SheetVersionProvider{

    /** Adds a new version of the sheet to the version history.
     * @param currentSheet the current state of the sheet.
     * @param numOfUpdateCells the number of cells that were updated in this version. */
    void addNewVersion(Sheet currentSheet, int numOfUpdateCells);

    /** Cleans the version history by removing all stored versions. */
    void cleanHistory();

    /** Returns the number of versions stored in the version history.
     * @return an integer representing the number of versions. */
    int getNumOfVersions();

    /** Returns the version history as a list of SheetVersionData objects.
     * @return a List of SheetVersionData objects representing the version history. */
    List<SheetVersionData> getVersionHistory();
}
