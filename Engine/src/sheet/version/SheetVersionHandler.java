package sheet.version;

import sheet.Sheet;

import java.util.List;

public interface SheetVersionHandler  extends SheetVersionProvider{
    void addNewVersion(Sheet currentSheet, int numOfUpdateCells);
    void cleanHistory();
    void printVersionsHistory();
    int getNumOfVersions();
    List<SheetVersionData> getVersionHistory();
}
