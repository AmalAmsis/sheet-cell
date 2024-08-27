package state;

import sheet.Sheet;
import sheet.version.SheetVersionHandler;
import sheet.version.SheetVersionHandlerImpl;

import java.io.Serializable;

public class SheetStateManagerImpl implements Serializable, SheetStateManager {
    private Sheet currentSheet;
    private SheetVersionHandler versionOfCurrentSheet;

    public SheetStateManagerImpl(Sheet currentSheet, SheetVersionHandler versionOfCurrentSheet) {
        this.currentSheet = currentSheet;
        this.versionOfCurrentSheet = versionOfCurrentSheet;
    }



    @Override
    public Sheet getCurrentSheet() {
        return currentSheet;
    }

    @Override
    public SheetVersionHandler getVersionHandler() {
        return versionOfCurrentSheet;
    }

    @Override
    public void updateSheetState(Sheet newVersionOfSheet, SheetVersionHandler versionOfCurrentSheet) {

    }
}
