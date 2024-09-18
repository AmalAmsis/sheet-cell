package sheet.command.filtersheet;

import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.Sheet;
import sheet.cell.Cell;
import sheet.command.filtersortdatapreparation.FilterSortPreparer;
import sheet.command.filtersortdatapreparation.FilterSortPreparerImpl;
import sheet.command.filtersortdatapreparation.ValidationException;
import sheet.coordinate.Coordinate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterSheetImpl implements FilterSheet {

    private FilterSortPreparer filterPreparer;

    public FilterSheetImpl(Sheet sheet) {
        this.filterPreparer = new FilterSortPreparerImpl(sheet);
    }

    public DTOSheet filterSheet(Map<String, List<String>> selectedColumnValues, String from, String to) throws ValidationException {

        Coordinate fromCoordinate = filterPreparer.convertStringToCoordinate(from);
        Coordinate toCoordinate = filterPreparer.convertStringToCoordinate(to);
        Character firstColInRange = fromCoordinate.getCol();

        filterPreparer.filterValidator(fromCoordinate,toCoordinate);

        List<List<Cell>> rowsInRange = filterPreparer.getRowInRange(fromCoordinate, toCoordinate);

        // סינון השורות על פי הערכים הנבחרים עבור כל עמודה
        List<List<Cell>> filteredRows = rowsInRange.stream()
                .filter(row -> {
                    // עבור כל שורה, נבדוק האם כל עמודה בטווח עומדת בתנאי הסינון
                    for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                        Cell cell = row.get(colIndex);

                        // מציאת שם העמודה הנוכחי (A, B, C...) לפי האינדקס
                        char columnName = (char) (fromCoordinate.getCol() + colIndex);

                        // בדיקה אם יש ערכים נבחרים לעמודה הזו
                        if (selectedColumnValues.containsKey(String.valueOf(columnName))) {
                            // רשימת הערכים הנבחרים לעמודה זו
                            List<String> allowedValues = selectedColumnValues.get(String.valueOf(columnName));

                            // אם הערך בתא לא נמצא ברשימה הנבחרת, נדחה את השורה כולה
                            if (!allowedValues.contains(cell.getEffectiveValue().getValue())) {
                                return false; // דוחה את השורה
                            }
                        }
                    }
                    return true; // אם השורה עומדת בתנאים, משאירים אותה
                })
                .collect(Collectors.toList());


        return new DTOSheetImpl(filteredRows,fromCoordinate,filterPreparer.getSheet());
    };

    private char getColumnName(int column, Character firstColInRange) {
        return (char)('A' + column - firstColInRange -1);
    }

}
