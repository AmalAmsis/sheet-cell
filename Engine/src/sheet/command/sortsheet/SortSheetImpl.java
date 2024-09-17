package sheet.command.sortsheet;

import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.Sheet;
import sheet.cell.Cell;
import sheet.command.filtersortdatapreparation.FilterSortPreparer;
import sheet.command.filtersortdatapreparation.FilterSortPreparerImpl;
import sheet.command.filtersortdatapreparation.ValidationException;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SortSheetImpl implements SortSheet {

    //copy -delete
    private FilterSortPreparer sortPreparer;

    //copy
    public SortSheetImpl(Sheet sheet) {
        this.sortPreparer = new FilterSortPreparerImpl(sheet);
    }

    @Override
    public DTOSheet getSortedSheet(String from, String to, List<Character> listOfColumnsPriorities) throws ValidationException {

        Coordinate fromCoordinate = convertStringToCoordinate(from);
        Coordinate toCoordinate = convertStringToCoordinate(to);
        Character firstColInRange = fromCoordinate.getCol();

        sortPreparer.sortValidator(fromCoordinate,toCoordinate,listOfColumnsPriorities);

        List<List<Cell>> ListOfSortedRows = sortPreparer.getRowInRange(fromCoordinate,toCoordinate).stream()
                .sorted((row1, row2) -> compareRows(row1, row2, listOfColumnsPriorities,firstColInRange))
                .collect(Collectors.toList());


        return new DTOSheetImpl(sortPreparer.getSheet(), ListOfSortedRows,fromCoordinate,toCoordinate);
    };



    private int compareRows(List<Cell> row1, List<Cell> row2, List<Character> listOfColumnsPriorities,Character firstColInRange) {
        List<Double> key1 = createSortingKey(row1, listOfColumnsPriorities,firstColInRange);
        List<Double> key2 = createSortingKey(row2, listOfColumnsPriorities,firstColInRange);

        // נבצע השוואה על כל מספר ברשימות לפי סדר העדיפויות
        for (int i = 0; i < key1.size(); i++) {
            Double value1 = key1.get(i);
            Double value2 = key2.get(i);

            System.out.println("Comparing values: " + value1 + " vs " + value2);

            // טיפול בתאים ריקים (אינסוף)
            if (value1.isInfinite() && !value2.isInfinite()) {
                return 1;  // אם value1 הוא אינסוף, value2 קודם
            } else if (!value1.isInfinite() && value2.isInfinite()) {
                return -1;  // אם value2 הוא אינסוף, value1 קודם
            } else {
                // השוואה רגילה של מספרים
                int comparison = value1.compareTo(value2);
                if (comparison != 0) {
                    System.out.println("Returning comparison result: " + comparison);
                    return comparison;  // אם יש הבדל, נחזיר את תוצאת ההשוואה
                }
            }
        }
        return 0;  // אם כל הערכים זהים, נחזיר 0 (שווה)
    }


    private String createCellKey(int col, int row) {
        return (char) ('A' + col) + Integer.toString(row);
    }


    private int getColumnIndex(Character column, Character firstColInRange) {
        return column - firstColInRange;
    }

    private List<Double> createSortingKey(List<Cell> row, List<Character> columns,Character firstColInRange ) {


        List<Double> sortingKey = new ArrayList<>();

        for (Character column : columns) {

            EffectiveValue effectiveValue = row.get(getColumnIndex(column,firstColInRange)).getEffectiveValue();

            // אם התא ריק או אינו מספרי, נחזיר אינסוף כדי למקם אותו אחרון
            if (effectiveValue.getCellType() != CellType.NUMERIC) {
                System.out.println("Non-numeric or empty cell found at column: " + column);
                sortingKey.add(Double.POSITIVE_INFINITY);  // תאים ריקים או לא מספריים יהיו בסוף המיון
            } else {
                try {
                    Double value = Double.parseDouble(effectiveValue.getValue().toString());
                    System.out.println("Sorting key for column " + column + ": " + value);
                    sortingKey.add(value);
                } catch (NumberFormatException e) {
                    System.out.println("Error converting value to Double at column: " + column);
                    sortingKey.add(Double.POSITIVE_INFINITY);  // במקרה של שגיאה בהמרה, נכניס אינסוף
                }
            }
        }

        return sortingKey;
    }


    public Coordinate convertStringToCoordinate(String stringCoordinate) {
        // Check if the input is null or of incorrect length
        if (stringCoordinate == null || stringCoordinate.length() < 2 || stringCoordinate.length() > 3) {
            throw new IllegalArgumentException("Input must be between 2 to 3 characters long and non-null.");
        }

        // get the col letter and checking that a letter representing the column is in the col range of the sheet
        char col = stringCoordinate.toUpperCase().charAt(0);


        //the follow must be a number
        for (int i = 1; i < stringCoordinate.length(); i++) {
            if (!Character.isDigit(stringCoordinate.charAt(i))) {
                throw new IllegalArgumentException("The input format is invalid. It should be a letter followed by digits.");
            }
        }

        // get row number
        int row;
        try {
            row = Integer.parseInt(stringCoordinate.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Row must be a valid number.");
        }

        // create the coordinate
        return new CoordinateImpl(col, row);
    }


}
