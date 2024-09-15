package sheet.sortsheet;

import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.Sheet;
import sheet.cell.Cell;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortSheetImpl {

    private Sheet sheet;

    public SortSheetImpl(Sheet sheet) {
        this.sheet = sheet;
    }

    public DTOSheet getSortedSheet(Coordinate from, Coordinate to, List<Character> listOfColumnsPriorities){

        List<List<Cell>> ListOfRows = getRowInRange(from,to).stream()
                .filter(row->isNumeric(row,listOfColumnsPriorities))
                .sorted((row1, row2) -> compareRows(row1, row2, listOfColumnsPriorities))
                .collect(Collectors.toList());
        return new DTOSheetImpl(sheet, ListOfRows,from,to);
    };

    private int compareRows(List<Cell> row1, List<Cell> row2, List<Character> listOfColumnsPriorities) {
        List<Double> key1 = createSortingKey(row1, listOfColumnsPriorities);
        List<Double> key2 = createSortingKey(row2, listOfColumnsPriorities);

        // נבצע השוואה על כל מספר ברשימות
        for (int i = 0; i < key1.size(); i++) {
            int comparison = key1.get(i).compareTo(key2.get(i));
            if (comparison != 0) {
                return comparison;  // אם יש הבדל, נחזיר את תוצאת ההשוואה
            }
        }
        return 0;  // אם כל הערכים זהים, נחזיר 0 (שווה)
    }
    private List<List<Cell>> getRowInRange(Coordinate from, Coordinate to){
        List<List<Cell>> listOfRows = new ArrayList<>();

        for(int row=from.getRow();row<=to.getRow();row++){
            List<Cell> currentRow = new ArrayList<>();
            for(char col = from.getCol();col<=to.getCol();col++){

                Coordinate currentCoordinate = new CoordinateImpl(col, row);

                Cell cell = sheet.getCell(currentCoordinate);
                currentRow.add(cell);
            }
            listOfRows.add(currentRow);
        }

        return listOfRows;
    }

    private String createCellKey(int col, int row) {
        return (char) ('A' + col) + Integer.toString(row);
    }

    private boolean isNumeric(List<Cell> row, List<Character> columns) {
        return columns.stream().allMatch(column -> {
            EffectiveValue effectiveValue = row.get(getColumnIndex(column)).getEffectiveValue();
            return effectiveValue.getCellType() == CellType.NUMERIC;
        });
    }

    private int getColumnIndex(Character column) {
        return column - 'A';
    }

    private List<Double> createSortingKey(List<Cell> row, List<Character> columns) {
        return columns.stream()
                .map(column -> {
                    EffectiveValue effectiveValue = row.get(getColumnIndex(column)).getEffectiveValue();
                    return (Double) effectiveValue.getValue();  // המרת הערך המספרי
                })
                .collect(Collectors.toList());
    }

}
