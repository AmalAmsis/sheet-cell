package sheet.command.filtersheet;

import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.Sheet;
import sheet.cell.Cell;
import sheet.command.filtersortdatapreparation.FilterSortPreparer;
import sheet.command.filtersortdatapreparation.FilterSortPreparerImpl;
import sheet.command.filtersortdatapreparation.ValidationException;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;

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
                    boolean rowShouldRemain = false;

                    // עבור כל שורה, נבדוק האם יש עמודה אחת לפחות שעומדת בתנאי הסינון
                    for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                        Cell cell = row.get(colIndex);
                        EffectiveValue effectiveValue = cell.getEffectiveValue();

                        // מציאת שם העמודה הנוכחי (A, B, C...) לפי האינדקס
                        char columnName = (char) (fromCoordinate.getCol() + colIndex);

                        System.out.println("Checking column: " + columnName + ", with value: " + effectiveValue.getValue());

                        // בדיקה אם יש ערכים נבחרים לעמודה הזו
                        if (selectedColumnValues.containsKey(String.valueOf(columnName))) {
                            // רשימת הערכים הנבחרים לעמודה זו
                            List<String> allowedValues = selectedColumnValues.get(String.valueOf(columnName));

                            System.out.println("Allowed values for column " + columnName + ": " + allowedValues);

                            // השוואה לפי סוג ערך
                            switch (effectiveValue.getCellType()) {
                                case NUMERIC:
                                    double numericValue = (Double) effectiveValue.getValue();
                                    rowShouldRemain = checkNumericMatch(numericValue, allowedValues);
                                    break;

                                case STRING:
                                    String stringValue = (String) effectiveValue.getValue();
                                    rowShouldRemain = allowedValues.contains(stringValue);
                                    break;

                                case BOOLEAN:
                                    boolean booleanValue = (Boolean) effectiveValue.getValue();
                                    rowShouldRemain = allowedValues.contains(String.valueOf(booleanValue));
                                    break;

                                case EMPTY:
                                    // אם התא ריק, נדלג עליו
                                    rowShouldRemain = false;
                                    break;
                            }

                            if (rowShouldRemain) {
                                System.out.println("Row remains due to match in column " + columnName);
                                break;
                            }
                        }
                    }

                    return rowShouldRemain; // נשאיר את השורה אם נמצאה עמודה שמתאימה
                })
                .collect(Collectors.toList());

        System.out.println("Number of rows after filtering: " + filteredRows.size());


        return new DTOSheetImpl(filteredRows,fromCoordinate,filterPreparer.getSheet());
    };

    private char getColumnName(int column, Character firstColInRange) {
        return (char)('A' + column - firstColInRange -1);
    }

    private boolean checkNumericMatch(double numericValue, List<String> allowedValues) {
        for (String allowedValue : allowedValues) {
            if (isNumeric(allowedValue) && numericValue == Double.parseDouble(allowedValue)) {
                return true;
            }
        }
        return false;
    }

    // פונקציה לבדוק אם ערך הוא מספרי
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }





}
