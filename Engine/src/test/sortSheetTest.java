package test;

import dto.DTOCell;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheetmanager.sheet.SheetImpl;
import sheetmanager.sheet.coordinate.Coordinate;
import sheetmanager.sheet.coordinate.CoordinateImpl;
import sheetmanager.sheet.command.sortsheet.SortSheetImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class sortSheetTest {

    public static void main(String[] args) {

        String title = "Test Sheet";
        int numOfRows = 5;
        int numOfCols = 5;
        int heightOfRows = 3;
        int widthOfCols = 10;


        SheetImpl sheet = new SheetImpl(title, numOfRows, numOfCols, heightOfRows, widthOfCols);


        sheet.addCell(new CoordinateImpl('A', 1),"5.0");   // A1
        sheet.addCell(new CoordinateImpl('A', 2), "3.0");   // A2
        sheet.addCell(new CoordinateImpl('A', 3),"7.0");   // A3
        sheet.addCell(new CoordinateImpl('A', 4), "3.0");   // A4


        sheet.addCell(new CoordinateImpl('B', 1), "2.0");   // B1
        sheet.addCell(new CoordinateImpl('B', 2), "8.0");   // B2
        sheet.addCell(new CoordinateImpl('B', 3),"1.0");   // B3
        sheet.addCell(new CoordinateImpl('B', 4), "7.0");   // B4


        sheet.addCell(new CoordinateImpl('C', 1),"DANIEL");   // C1
        sheet.addCell(new CoordinateImpl('C', 2), "YARDEN");   // C2
        sheet.addCell(new CoordinateImpl('C', 3), "YES");   // C3
        sheet.addCell(new CoordinateImpl('C', 4),"BLAA" );   // C4


        DTOSheet beforeSort = new DTOSheetImpl(sheet);
        printSheetToConsole(beforeSort);




        SortSheetImpl sortSheet = new SortSheetImpl(sheet);

        // הגדרת טווח למיון (מ-A1 עד C3)
        Coordinate from = new CoordinateImpl('A', 1);
        Coordinate to = new CoordinateImpl('C', 4);

        List<Character> listOfColumnsPriorities = new ArrayList<>();
        listOfColumnsPriorities.add('A');
        listOfColumnsPriorities.add('B');



//        try {
//            DTOSheet sortedSheet = sortSheet.getSortedSheet(from, to, listOfColumnsPriorities);
//
//            System.out.println("Sorted sheet by column A then B :");
//            printSheetToConsole(sortedSheet);
//        } catch (RangeValidationException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
    }

    // Prints the entire sheet to the console
    private static void printSheetToConsole(DTOSheet dtoSheet) {
        StringBuilder sb = new StringBuilder();

        //Adding the title and the version of the sheet to the StringBuilder
        printSheetHeader(dtoSheet, sb);

        int numOfColumns = dtoSheet.getNumOfColumns();
        int numOfRows = dtoSheet.getNumOfRows();
        int widthOfColumn = dtoSheet.getWidthOfColumns();
        int heightOfRow = dtoSheet.getHeightOfRows();

        // Adding the top row that represents the columns to the StringBuilder
        printColumnHeaders(numOfColumns, widthOfColumn, sb);

        // Iterating over each row to print its content
        Map<String, DTOCell> cells = dtoSheet.getCells();
        for (int row = 1; row <= numOfRows; row++) {
            printRow(row, numOfColumns, widthOfColumn, cells, sb,heightOfRow);
        }

        // Printing the final output to the console
        System.out.println();
        System.out.println(sb.toString());
    }

    // Add the title and the version of the sheet to the StringBuilder
    private static void printSheetHeader(DTOSheet dtoSheet, StringBuilder sb) {
        sb.append("Sheet Title: ").append(dtoSheet.getSheetTitle()).append("\n")
                .append("Sheet Version: ").append(dtoSheet.getSheetVersion()).append("\n\n");
    }
    // Add the column headers (A, B, C, etc.) to the StringBuilder
    private static void printColumnHeaders(int numOfColumns, int widthOfColumn, StringBuilder sb) {
        sb.append("    |");  //Leaving a space of 4 characters for line numbers
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
    // Add each row, including the row number and cell values to the StringBuilder
    private static void printRow(int row, int numOfColumns, int widthOfColumn, Map<String, DTOCell> cells, StringBuilder sb, int heightOfRow) {

        sb.append(" ").append(String.format("%02d", row)).append(" |"); //print the number of the row
        for (int col = 0; col < numOfColumns; col++) {
            //create the key of yhe cell
            char columnLetter = (char) ('A' + col);
            String cellKey = columnLetter + ":" + row;

            // Get the DTOCell associated with the key
            DTOCell currentCell = cells.get(cellKey);
            String currentCellValue;
            StringBuilder sbCellValue = new StringBuilder();

            // If the cell exists
            if (currentCell != null) {
                // Get the effective value as a string
                currentCellValue = currentCell.getEffectiveValue().toString();

                if (currentCellValue.length() > widthOfColumn) {  // If the value is too long, cut it to fit the cell width
                    currentCellValue = currentCellValue.substring(0, widthOfColumn);
                }
                int cellPadding = widthOfColumn - currentCellValue.length();
                sbCellValue.append(currentCellValue).append(" ".repeat(cellPadding));

            } else { // If the cell does not exist, fill with spaces
                sbCellValue.append(" ".repeat(widthOfColumn));
            }
            // Add "|" between columns, except after the last column
            if (col < numOfColumns - 1) {
                sbCellValue.append("|");
            }
            sb.append(sbCellValue);
        }
        sb.append("\n");  // Move to the next line after each row
        for(int heigh = 0; heigh < heightOfRow-1; heigh++) {
            printEmptyRow(sb, widthOfColumn,numOfColumns);
        }
    }
    // Prints an empty row (used for formatting purposes)
    private static void printEmptyRow(StringBuilder sb, int widthOfColumn, int numOfColumns) {
        sb.append("    |");
        for (int col = 0; col < numOfColumns; col++) {
            sb.append(" ".repeat(widthOfColumn));
            if (col < numOfColumns - 1) {
                sb.append("|");
            }
        }
        sb.append("\n");  // Move to the next line after each row
    }
}
