package sheet.command.sortsheet;

import dto.DTOSheet;
import sheet.command.filtersortdatapreparation.ValidationException;

import java.util.List;

public interface SortSheet {
    DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities) throws ValidationException;

}
