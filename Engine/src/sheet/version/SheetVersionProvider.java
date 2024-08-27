package sheet.version;

import dto.DTOSheet;
import sheet.Sheet;

public interface SheetVersionProvider  {
    DTOSheet getSheetByVersion(int version);
}
