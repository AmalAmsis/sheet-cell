package sheet.sortsheet;

import dto.DTOSheet;
import sheet.coordinate.Coordinate;

import java.util.List;

public interface SortSheet {
    DTOSheet getSortedSheet(Coordinate from, Coordinate to, List<Character> listOfColumnsPriorities);

}
