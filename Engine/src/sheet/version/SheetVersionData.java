package sheet.version;

import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.Sheet;

public class SheetVersionData {
    DTOSheet dtoSheet;
    int numOfUpdateCells;

    public SheetVersionData(Sheet currentSheet, int numOfUpdateCells){
    this.numOfUpdateCells = numOfUpdateCells;
    this.dtoSheet = new DTOSheetImpl(currentSheet);
    }
}







