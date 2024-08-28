package sheet.version;

import dto.DTOSheet;
import sheet.Sheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SheetVersionHandlerImpl implements SheetVersionHandler, Serializable {

    private final List<SheetVersionData> versionHistory;
    private int numOfVersions;



    public SheetVersionHandlerImpl(Sheet currentSheet, int numOfUpdateCells) {
        this.versionHistory = new ArrayList<>();
        this.versionHistory.add(new SheetVersionData(currentSheet,numOfUpdateCells));
        this.numOfVersions = 1;
    }

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
    public void printVersionsHistory() {
        StringBuilder sb = new StringBuilder();

        // Print header for version numbers
        sb.append("Version number:         |");
        for (SheetVersionData version : this.versionHistory) {
            sb.append(" ").append(version.dtoSheet.getSheetVersion()).append(" |");
        }
        sb.append("\n");

        // Print header for number of cell changes
        sb.append("Number of cell changes: |");
        for (SheetVersionData version : this.versionHistory) {
            sb.append(" ").append(version.numOfUpdateCells).append(" |");
        }
        sb.append("\n");

        // Print the result
        System.out.println(sb.toString());
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
        return this.versionHistory.get(version-1).dtoSheet;
    }
}
