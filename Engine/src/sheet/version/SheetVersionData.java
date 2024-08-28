package sheet.version;

import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.Sheet;

import java.io.Serializable;

public class SheetVersionData implements Serializable {
    private DTOSheet dtoSheet;
    private int numOfUpdateCells;

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







