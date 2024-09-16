package sheet.sortsheet;

import dto.DTOSheet;
import sheet.coordinate.Coordinate;

import java.util.List;

public interface SortSheet {
    DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities) throws RangeValidationException;

}
