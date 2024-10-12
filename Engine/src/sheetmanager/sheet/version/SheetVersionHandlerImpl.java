package sheetmanager.sheet.version;

import dto.DTOSheet;
import sheetmanager.sheet.Sheet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SheetVersionHandlerImpl is an implementation of the SheetVersionHandler interface.
 * This class manages the version history of a spreadsheet, including adding new versions,
 * retrieving version data, and cleaning the version history. It supports serialization.
 */
public class SheetVersionHandlerImpl implements SheetVersionHandler, Serializable {

    private final List<SheetVersionData> versionHistory; // List storing the version history of the sheet
    private int numOfVersions; // The total number of versions stored

    /** Constructs a SheetVersionHandlerImpl with the initial sheet version.
     * @param currentSheet the initial state of the sheet.
     * @param numOfUpdateCells the number of cells that were updated in this version. */
    public SheetVersionHandlerImpl(Sheet currentSheet, int numOfUpdateCells) {
        this.versionHistory = new ArrayList<>();
        this.versionHistory.add(new SheetVersionData(currentSheet,numOfUpdateCells));
        this.numOfVersions = 1;
    }

    /** Returns the version history as a list of SheetVersionData objects.
     * @return a List of SheetVersionData objects representing the version history. */
    public List<SheetVersionData> getVersionHistory() {
        return this.versionHistory;
    }

    @Override
    public void addNewVersion(Sheet currentSheet, int numOfUpdateCells) {
        this.versionHistory.add(new SheetVersionData(currentSheet,numOfUpdateCells));
        this.numOfVersions++;
    }

    @Override
    public void cleanHistory() {
        this.versionHistory.clear();
        this.numOfVersions = 0;
    }

    @Override
    public int getNumOfVersions() {
        return numOfVersions;
    }

    @Override
    public DTOSheet getSheetByVersion(int version) {
        if (version < 0 || version > this.numOfVersions) {
            throw new IllegalArgumentException("Invalid version number: " + version + ". Please choose a version number between 1 and " + (this.numOfVersions-1) +" (inclusive).");
        }
        return this.versionHistory.get(version-1).getDtoSheet();
    }
}
