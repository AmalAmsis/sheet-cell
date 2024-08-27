package sheet.version;

import sheet.Sheet;

public interface SheetVersionHandler  extends SheetVersionProvider{
    void addNewVersion(Sheet currentSheet, int numOfUpdateCells);
    void cleanHistory();
    void printVersionsHistory();
    int getNumOfVersions();
}
