package sheet;

import dto.DTOSheet;
import jaxb.schema.generated.STLSheet;
import sheet.cell.Cell;
import sheet.coordinate.Coordinate;

public interface Sheet extends SheetDataModifier, SheetDataRetriever, SheetDependencyManager {
    Cell getCell(Coordinate coordinate);
    STLSheet convertFromSheetToStlSheet();
    Coordinate convertStringToCoordinate(String stringCoordinate);
}
