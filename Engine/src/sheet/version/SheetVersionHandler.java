package sheet.version;

import sheet.Sheet;

public interface SheetVersionHandler  extends SheetVersionProvider{
    void addNewVersion(Sheet dtoSheet);
    void cleanHistory();
}
