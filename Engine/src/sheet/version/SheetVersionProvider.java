package sheet.version;

import dto.DTOSheet;

public interface SheetVersionProvider  {
    DTOSheet getSheetByVersion(int version);
}
