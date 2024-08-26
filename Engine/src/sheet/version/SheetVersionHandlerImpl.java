package sheet.version;

import sheet.Sheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SheetVersionHandlerImpl implements SheetVersionHandler, Serializable {

    private final List<Sheet> versionHistory;
    private int numOfVersions;

    public SheetVersionHandlerImpl(Sheet currentSheet) {
        this.versionHistory = new ArrayList<Sheet>();
        this.versionHistory.add(currentSheet);
        this.numOfVersions = 1;
    }

    @Override
    public void addNewVersion(Sheet dtoSheet) {
        this.versionHistory.add(dtoSheet);
        this.numOfVersions++;
    }

    @Override
    public void cleanHistory() {
        this.versionHistory.clear();
        this.numOfVersions = 0;
    }

    @Override
    public Sheet getSheetByVersion(int version) {
        if (version < 0 || version > this.numOfVersions) {
            throw new IllegalArgumentException("Invalid version number: " + version + ". Please choose a version number between 1 and " + (this.numOfVersions-1) +" (inclusive).");
        }
        return this.versionHistory.get(version-1);
    }
}