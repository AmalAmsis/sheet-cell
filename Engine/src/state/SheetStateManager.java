package state;

import sheet.Sheet;
import sheet.version.SheetVersionHandler;
import sheet.version.SheetVersionHandlerImpl;

import java.io.IOException;

public interface SheetStateManager {
    //This object is at the engine level - as soon as a file is loaded into the system,
    // an object of type sheetStateManager is created for it

    Sheet getCurrentSheet();
    SheetVersionHandler getVersionHandler();
}
