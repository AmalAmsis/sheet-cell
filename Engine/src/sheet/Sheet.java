package sheet;

import dto.DTOSheet;
import sheet.cell.Cell;
import sheet.coordinate.Coordinate;

public interface Sheet extends SheetDataModifier, SheetDataRetriever, SheetDependencyManager {
    Cell getCell(Coordinate coordinate);
    DTOSheet convertToDTOSheet();
}
