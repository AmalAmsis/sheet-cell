package sheet;

import jaxb.schema.generated.STLSheet;
import sheet.cell.Cell;
import sheet.coordinate.Coordinate;

public interface Sheet {
    int getVersion();
    String getTitle();
    Cell getCell(Coordinate coordinate);
    void setCell(Coordinate coordinate, String originalValue);
    STLSheet convertFromSheetToStlSheet();
}
