package sheetmanager.state;

import sheetmanager.sheet.Sheet;
import sheetmanager.sheet.version.SheetVersionHandler;
import java.io.Serializable;


/**
 * SheetStateManagerImpl is an implementation of the SheetStateManager interface.
 * It manages the current state of a spreadsheet, including the sheet itself and its version history.
 * This class supports serialization for saving and loading the state.
 */
public class SheetStateManagerImpl implements Serializable, SheetStateManager {

    private Sheet currentSheet; // The current sheet being managed
    private SheetVersionHandler versionOfCurrentSheet; // The version handler for the current sheet

    /** Constructs a SheetStateManagerImpl with the provided sheet and version handler.
     * @param currentSheet the initial state of the sheet.
     * @param versionOfCurrentSheet the version handler associated with the sheet. */
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

}
