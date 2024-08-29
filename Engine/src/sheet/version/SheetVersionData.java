package sheet.version;

import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.Sheet;
import java.io.Serializable;


/**
 * SheetVersionData stores versioned data of a spreadsheet.
 * It contains a DTO representation of the sheet and the number of cells that were updated.
 * This class supports serialization for saving and loading versioned data.
 */
public class SheetVersionData implements Serializable {
    private DTOSheet dtoSheet; // DTO representation of the sheet
    private int numOfUpdateCells; // Number of cells that were updated

    /** Constructs a SheetVersionData object with the current sheet and the number of updated cells.
     * @param currentSheet the current state of the sheet.
     * @param numOfUpdateCells the number of cells that were updated in this version. */
    public SheetVersionData(Sheet currentSheet, int numOfUpdateCells){
    this.numOfUpdateCells = numOfUpdateCells;
    this.dtoSheet = new DTOSheetImpl(currentSheet);
    }

    public DTOSheet getDtoSheet() {
        return dtoSheet;
    }

    public int getNumOfUpdateCells(){
        return numOfUpdateCells;
    }
}







