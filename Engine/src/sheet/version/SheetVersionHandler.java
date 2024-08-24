package sheet.version;

import dto.DTOSheet;

public interface SheetVersionHandler  extends SheetVersionProvider{
    void addNewVersion(DTOSheet dtoSheet);
    void cleanHistory();
}
