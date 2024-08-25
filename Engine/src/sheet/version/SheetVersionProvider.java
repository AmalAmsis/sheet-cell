package sheet.version;

import sheet.Sheet;

public interface SheetVersionProvider  {
    Sheet getSheetByVersion(int version);
}
