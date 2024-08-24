package sheet.version;

import dto.DTOSheet;

import java.util.ArrayList;
import java.util.List;

public class SheetVersionHandlerImpl implements SheetVersionHandler {

    private final List<DTOSheet> versionHistory;
    private int numOfVersions;

    public SheetVersionHandlerImpl() {
        this.versionHistory = new ArrayList<DTOSheet>();
        this.numOfVersions = 0;
    }

    @Override
    public void addNewVersion(DTOSheet dtoSheet) {
        this.versionHistory.add(dtoSheet);
        this.numOfVersions++;
    }

    @Override
    public void cleanHistory() {
        this.versionHistory.clear();
        this.numOfVersions = 0;
    }

    @Override
    public DTOSheet getSheetByVersion(int version) {
        if (version < 0 || version > this.numOfVersions) {
            throw new IllegalArgumentException("Invalid version number: " + version + ". Please choose a version number between 1 and " + (this.numOfVersions-1) +" (inclusive).");
        }
        return this.versionHistory.get(version-1);
    }
}
