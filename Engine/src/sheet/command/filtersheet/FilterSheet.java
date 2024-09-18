package sheet.command.filtersheet;

import dto.DTOSheet;
import sheet.command.filtersortdatapreparation.ValidationException;

import java.util.List;
import java.util.Map;

public interface FilterSheet {
    DTOSheet filterSheet(Map<String, List<String>> selectedColumnValues,String from, String to) throws ValidationException;
}
