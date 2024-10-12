package sheetmanager.sheet.command.filtersortdatapreparation;

import sheetmanager.sheet.Sheet;
import sheetmanager.sheet.cell.Cell;
import sheetmanager.sheet.coordinate.Coordinate;

import java.util.List;

public interface FilterSortPreparer {

    void sortValidator(Coordinate from, Coordinate to , List<Character> listOfColumnsPriorities)throws ValidationException;
    void filterValidator(Coordinate from, Coordinate to ) throws ValidationException;
    List<List<Cell>> getRowInRange(Coordinate from, Coordinate to);
    Sheet getSheet();
    Coordinate convertStringToCoordinate(String stringCoordinate);
}
