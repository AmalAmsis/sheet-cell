package sheetmanager.sheet.range;

import dto.DTOCell;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheetmanager.sheet.Sheet;
import sheetmanager.sheet.SheetImpl;
import sheetmanager.sheet.coordinate.Coordinate;
import sheetmanager.sheet.coordinate.CoordinateImpl;

import java.util.Map;

public class Test {

    // Prints the entire sheet to the console
    public static void printSheetToConsole(DTOSheet dtoSheet) {
        StringBuilder sb = new StringBuilder();
        printSheetHeader(dtoSheet, sb);
        printColumnHeaders(dtoSheet, sb);
        printRows(dtoSheet, sb);
        System.out.println();
        System.out.println(sb.toString());
    }

    private static void printSheetHeader(DTOSheet dtoSheet, StringBuilder sb) {
        sb.append("Sheet Title: ").append(dtoSheet.getSheetTitle()).append("\n")
                .append("Sheet Version: ").append(dtoSheet.getSheetVersion()).append("\n\n");
    }

    private static void printColumnHeaders(DTOSheet dtoSheet, StringBuilder sb) {
        int numOfColumns = dtoSheet.getNumOfColumns();
        int widthOfColumn = dtoSheet.getWidthOfColumns();
        sb.append("    |");  // Leaving space for line numbers
        for (int col = 0; col < numOfColumns; col++) {
            char columnLetter = (char) ('A' + col);
            int paddingBefore = (widthOfColumn - 1) / 2;
            int paddingAfter = widthOfColumn - 1 - paddingBefore;
            sb.append(" ".repeat(paddingBefore)).append(columnLetter).append(" ".repeat(paddingAfter));
            if (col < numOfColumns - 1) {
                sb.append("|");
            }
        }
        sb.append("\n");
    }

    private static void printRows(DTOSheet dtoSheet, StringBuilder sb) {
        Map<String, DTOCell> cells = dtoSheet.getCells();
        int numOfRows = dtoSheet.getNumOfRows();
        int numOfColumns = dtoSheet.getNumOfColumns();
        int widthOfColumn = dtoSheet.getWidthOfColumns();
        int heightOfRow = dtoSheet.getHeightOfRows();

        for (int row = 1; row <= numOfRows; row++) {
            printRow(row, numOfColumns, widthOfColumn, cells, sb, heightOfRow);
        }
    }

    private static void printRow(int row, int numOfColumns, int widthOfColumn, Map<String, DTOCell> cells, StringBuilder sb, int heightOfRow) {
        sb.append(" ").append(String.format("%02d", row)).append(" |"); // Print row number
        for (int col = 0; col < numOfColumns; col++) {
            char columnLetter = (char) ('A' + col);
            String cellKey = columnLetter + ":" + row;

            DTOCell currentCell = cells.get(cellKey);
            String currentCellValue;
            StringBuilder sbCellValue = new StringBuilder();

            if (currentCell != null) {
                currentCellValue = currentCell.getEffectiveValue().toString();
                if (currentCellValue.length() > widthOfColumn) {
                    currentCellValue = currentCellValue.substring(0, widthOfColumn);
                }
                int cellPadding = widthOfColumn - currentCellValue.length();
                sbCellValue.append(currentCellValue).append(" ".repeat(cellPadding));
            } else {
                sbCellValue.append(" ".repeat(widthOfColumn));
            }

            if (col < numOfColumns - 1) {
                sbCellValue.append("|");
            }
            sb.append(sbCellValue);
        }
        sb.append("\n");
        for (int heigh = 0; heigh < heightOfRow - 1; heigh++) {
            printEmptyRow(sb, widthOfColumn, numOfColumns);
        }
    }

    private static void printEmptyRow(StringBuilder sb, int widthOfColumn, int numOfColumns) {
        sb.append("    |");
        for (int col = 0; col < numOfColumns; col++) {
            sb.append(" ".repeat(widthOfColumn));
            if (col < numOfColumns - 1) {
                sb.append("|");
            }
        }
        sb.append("\n");
    }

    public static void main(String[] args) {
        testAddRange();
        testAddRange2();
        testAddRange3();
        testAddRange4();
        testAddRange5();
        testAddRange6();
        testRemoveRange();
        testInvalidRangeFormat();
        testRangeOverlap();
        testCellUpdatesAfterRangeRemoval();
    }

    private static void testAddRange() {
        System.out.println("Running testAddRange...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        Coordinate coordinate00 = new CoordinateImpl('A', 1);
        Coordinate coordinate01 = new CoordinateImpl('A', 2);
        Coordinate coordinate02 = new CoordinateImpl('A', 3);
        Coordinate coordinate03 = new CoordinateImpl('A', 4);
        Coordinate coordinate22 = new CoordinateImpl('C', 3);
        Coordinate coordinate23 = new CoordinateImpl('C', 4);

        sheet.setCell(coordinate00, "1");
        sheet.setCell(coordinate01, "2");
        sheet.setCell(coordinate02, "{PLUS,5,5}");
        sheet.setCell(coordinate03, "{PLUS,5,3}");

        try {
            sheet.addRangeToManager("rangeA", "A1..A4");
            sheet.setCell(coordinate22, "{SUM,rangeA}");
            sheet.setCell(coordinate23,"{Average,rangeA}");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }

    private static void testAddRange2() {
        System.out.println("Running testAddRange2...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        Coordinate coordinate00 = new CoordinateImpl('A', 1);
        Coordinate coordinate01 = new CoordinateImpl('A', 2);
        Coordinate coordinate02 = new CoordinateImpl('A', 3);
        Coordinate coordinate03 = new CoordinateImpl('A', 4);
        Coordinate coordinate22 = new CoordinateImpl('C', 3);

        sheet.setCell(coordinate00, "1");
        sheet.setCell(coordinate01, "2");
        sheet.setCell(coordinate02, "{PLUS,5,5}");
        sheet.setCell(coordinate03, "{PLUS,5,3}");

        try {
            sheet.addRangeToManager("rangeA", "A1..A4");
            sheet.setCell(coordinate22, "{SUM,rangeA}");
            sheet.setCell(coordinate01, "{Average,rangeA}");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }

    private static void testAddRange3() {
        System.out.println("Running testAddRange3...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        Coordinate coordinate00 = new CoordinateImpl('A', 1);
        Coordinate coordinate01 = new CoordinateImpl('A', 2);
        Coordinate coordinate02 = new CoordinateImpl('A', 3);
        Coordinate coordinate03 = new CoordinateImpl('A', 4);
        Coordinate coordinate22 = new CoordinateImpl('C', 3);

        sheet.setCell(coordinate00, "1");
        sheet.setCell(coordinate01, "2");
        sheet.setCell(coordinate02, "{PLUS,5,5}");
        sheet.setCell(coordinate03, "{PLUS,5,3}");

        try {
            sheet.addRangeToManager("rangeA", "A1..A4");
            sheet.setCell(coordinate22, "{SUM,rangeA}");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }

    private static void testAddRange4() {
        System.out.println("Running testAddRange...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        Coordinate coordinate00 = new CoordinateImpl('A', 1);
        Coordinate coordinate01 = new CoordinateImpl('A', 2);
        Coordinate coordinate02 = new CoordinateImpl('B', 1);
        Coordinate coordinate03 = new CoordinateImpl('B', 2);
        Coordinate coordinate22 = new CoordinateImpl('C', 3);
        Coordinate coordinate23 = new CoordinateImpl('C', 4);

        sheet.setCell(coordinate00, "1");
        sheet.setCell(coordinate01, "2");
        sheet.setCell(coordinate02, "{PLUS,5,5}");
        sheet.setCell(coordinate03, "{PLUS,5,3}");

        try {
            sheet.addRangeToManager("rangeA", "A1..B2");
            sheet.setCell(coordinate22, "{SUM,rangeA}");
            sheet.setCell(coordinate23, "{Average,rangeA}");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }

    private static void testAddRange5() {
        System.out.println("Running testAddRange5...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        Coordinate coordinate00 = new CoordinateImpl('A', 1);
        Coordinate coordinate01 = new CoordinateImpl('B', 1);
        Coordinate coordinate02 = new CoordinateImpl('C', 1);
        Coordinate coordinate03 = new CoordinateImpl('D', 1);
        Coordinate coordinate22 = new CoordinateImpl('C', 3);
        Coordinate coordinate23 = new CoordinateImpl('D', 3);

        sheet.setCell(coordinate00, "5");
        sheet.setCell(coordinate01, "5");
        sheet.setCell(coordinate02, "{PLUS,5,5}");
        sheet.setCell(coordinate03, "{PLUS,5,5}");

        try {
            sheet.addRangeToManager("rangeA", "A1..D1");
            sheet.setCell(coordinate22, "{SUM,rangeA}");
            sheet.setCell(coordinate23, "{Average,rangeA}");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }

    private static void testAddRange6() {
        System.out.println("Running testAddRange6...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        Coordinate coordinate00 = new CoordinateImpl('A', 1);
        Coordinate coordinate01 = new CoordinateImpl('A', 2);
        Coordinate coordinate02 = new CoordinateImpl('A', 3);
        Coordinate coordinate03 = new CoordinateImpl('A', 4);
        Coordinate coordinate22 = new CoordinateImpl('C', 3);
        Coordinate coordinate23 = new CoordinateImpl('C', 4);

        sheet.setCell(coordinate00, "1");
        sheet.setCell(coordinate01, "2");
        sheet.setCell(coordinate02, "{PLUS,5,5}");
        sheet.setCell(coordinate03, "{PLUS,5,3}");

        try {
            sheet.addRangeToManager("rangeA", "A1..B5");
            sheet.setCell(coordinate22, "{SUM,rangeA}");
            sheet.setCell(coordinate23,"{Average,rangeA}");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }

    private static void testRemoveRange() {
        System.out.println("Running testRemoveRange...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        Coordinate coordinate00 = new CoordinateImpl('A', 1);
        Coordinate coordinate01 = new CoordinateImpl('A', 2);
        Coordinate coordinate02 = new CoordinateImpl('A', 3);
        Coordinate coordinate03 = new CoordinateImpl('A', 4);
        Coordinate coordinate22 = new CoordinateImpl('C', 3);

        sheet.setCell(coordinate00, "1");
        sheet.setCell(coordinate01, "2");
        sheet.setCell(coordinate02, "{PLUS,5,5}");
        sheet.setCell(coordinate03, "{PLUS,5,3}");

        try {
            sheet.addRangeToManager("rangeA", "A1..A4");
            sheet.setCell(coordinate22, "{SUM,rangeA}");
            sheet.removeRangeFromManager("rangeA");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }

    private static void testInvalidRangeFormat() {
        System.out.println("Running testInvalidRangeFormat...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        try {
            sheet.addRangeToManager("invalidRange", "A1-A4");  // Invalid format
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }

    private static void testRangeOverlap() {
        System.out.println("Running testRangeOverlap...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        Coordinate coordinate00 = new CoordinateImpl('A', 1);
        Coordinate coordinate01 = new CoordinateImpl('A', 2);
        Coordinate coordinate02 = new CoordinateImpl('A', 3);
        Coordinate coordinate03 = new CoordinateImpl('A', 4);
        Coordinate coordinate22 = new CoordinateImpl('C', 3);

        sheet.setCell(coordinate00, "1");
        sheet.setCell(coordinate01, "2");
        sheet.setCell(coordinate02, "{PLUS,5,5}");
        sheet.setCell(coordinate03, "{PLUS,5,3}");

        try {
            sheet.addRangeToManager("rangeA", "A1..A4");
            sheet.addRangeToManager("rangeB", "A2..A3"); // Overlapping range
            sheet.setCell(coordinate22, "{SUM,rangeB}");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }

    private static void testCellUpdatesAfterRangeRemoval() {
        System.out.println("Running testCellUpdatesAfterRangeRemoval...");
        Sheet sheet = new SheetImpl("Test Sheet", 5, 5, 1, 10);

        Coordinate coordinate00 = new CoordinateImpl('A', 1);
        Coordinate coordinate01 = new CoordinateImpl('A', 2);
        Coordinate coordinate02 = new CoordinateImpl('A', 3);
        Coordinate coordinate03 = new CoordinateImpl('A', 4);
        Coordinate coordinate22 = new CoordinateImpl('C', 3);

        sheet.setCell(coordinate00, "1");
        sheet.setCell(coordinate01, "2");
        sheet.setCell(coordinate02, "{PLUS,5,5}");
        sheet.setCell(coordinate03, "{PLUS,5,3}");

        try {
            sheet.addRangeToManager("rangeA", "A1..A4");
            sheet.setCell(coordinate22, "{SUM,rangeA}");
            sheet.removeRangeFromManager("rangeA");
            // Check the effect of range removal on cells
            String cellValue = sheet.getCell(coordinate22).getEffectiveValue().toString();
            System.out.println("Value of cell C3 after range removal: " + cellValue);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DTOSheet dtoSheet = new DTOSheetImpl(sheet);
        printSheetToConsole(dtoSheet);
    }
}
