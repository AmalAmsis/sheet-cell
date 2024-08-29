package state;

import sheet.Sheet;
import sheet.version.SheetVersionHandler;


/**
 * SheetStateManager manages the state of a spreadsheet within the engine.
 * It is responsible for tracking the current sheet, managing version history,
 * and updating the state of the sheet as changes occur.
 * This object is created as soon as a file is loaded into the system.
 */
public interface SheetStateManager  {

    /** Retrieves the current sheet being managed.
     * @return the current Sheet object. */
    Sheet getCurrentSheet();

    /** Retrieves the version handler associated with the current sheet.
     * @return a SheetVersionHandler that manages the version history of the sheet. */
    SheetVersionHandler getVersionHandler();

}
