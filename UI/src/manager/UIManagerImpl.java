package manager;

import dto.DTOCell;
import dto.DTOSheet;
import engine.Engine;
import engine.EngineImpl;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import jakarta.xml.bind.JAXBException;
import jaxb.schema.xmlprocessing.FileDataException;
import menu.Command;

public class UIManagerImpl implements UIManager {

    Engine engine;

    public UIManagerImpl() {
        engine = new EngineImpl();
    }

    @Override
    public void printMenu() {
        System.out.println("\n===============================");
        System.out.println("          Main Menu            ");
        System.out.println("===============================\n");

        for (Command command : Command.values()) {
            System.out.printf("%s\n", command.getDescription());
        }

        System.out.println("\n===============================");
        System.out.print("Please select an option (1-" + Command.values().length + "): ");
        System.out.println();
    }


    @Override
    public void displaySheet(){
        DTOSheet dtoSheet = engine.displaySheet();
        printSheetToConsole(dtoSheet);
    }

    @Override
    public void displayCell() {

    }

    //@Override
    public void loadXmlFile(String filePath) {
        try{
            // Attempt to load the sheet from the XML file
            engine.loadSheetFromXmlFile(filePath);
            // If successful, print a success message
            System.out.println("The XML file was loaded successfully.\n");

        } catch (IllegalArgumentException e) {
            // Handle errors from isXmlFile (e.g., invalid file path or non-XML file)
            System.err.println("Error: The XML file specifies an" + e.getMessage());

        } catch (FileDataException.InvalidRowCountException e) {
            // Print a specific error message for invalid row count
            System.err.println("Error: The XML file specifies an " + e.getMessage() + ".\nPlease ensure the sheet has between 1 and 50 rows.\n");

        } catch (FileDataException.InvalidColumnCountException e) {
            // Print a specific error message for invalid column count
            System.err.println("Error: The XML file specifies an " + e.getMessage() + ".\nPlease ensure the sheet has between 1 and 20 columns.\n");

        } catch (FileDataException.InvalidColumnWidthException e) {
            // Print a specific error message for invalid column width
            System.err.println("Error: The XML file specifies an " + e.getMessage() + ".\nPlease ensure the column width is a positive number.\n");

        } catch (FileDataException.InvalidRowHeightException e) {
            // Print a specific error message for invalid row height
            System.err.println("Error: The XML file specifies an " + e.getMessage() + ".\nPlease ensure the row height is a positive number.\n");

        } catch (FileDataException.CellOutOfBoundsException e) {
            // Print a specific error message for cell out of bounds
            System.err.println("Error: One or more cells in the file are outside the valid sheet boundaries.\n.");

        } catch (FileDataException.CircularReferenceException e) {
            // Print a specific error message for circular reference
            System.err.println("Error: The file contains a circular reference, which is not allowed.\n");

        } catch (FileNotFoundException e) {
            // Handle file not found
            System.err.println("Error: The file was not found at the specified path: " + filePath +"\n");

        } catch (JAXBException e) {
            System.err.println("Error: Failed to process the XML file. \nThe file might be corrupted or invalid.\n");

        } catch (Exception e) {
            // Catch any other exceptions
            System.err.println("Error: An unexpected error occurred: " + e.getMessage());
        }

    }

    @Override
    public void loadSeralizationFile() {

    }

    @Override
    public void saveSeralizationFile() {

    }

    //להחזיר לפרייבט
    public static void printSheetToConsole(DTOSheet dtoSheet) {
        StringBuilder sb = new StringBuilder();

        //Adding the title and the version of the sheet to the StringBuilder
        printSheetHeader(dtoSheet, sb);

        int numOfColumns = dtoSheet.getNumOfColumns();
        int numOfRows = dtoSheet.getNumOfRows();
        int widthOfColumn = dtoSheet.getWidthOfColumns();

        // Adding the top row that represents the columns to the StringBuilder
        printColumnHeaders(numOfColumns, widthOfColumn, sb);

        // Iterating over each row to print its content
        Map<String, DTOCell> cells = dtoSheet.getCells();
        for (int row = 1; row <= numOfRows; row++) {
            printRow(row, numOfColumns, widthOfColumn, cells, sb);
        }

        // Printing the final output to the console
        System.out.println(sb.toString());
    }

    // Adding the title and the version of the sheet to the StringBuilder
    public static void printSheetHeader(DTOSheet dtoSheet, StringBuilder sb) {
        sb.append("Sheet Title: ").append(dtoSheet.getSheetTitle()).append("\n")
                .append("Sheet Version: ").append(dtoSheet.getSheetVersion()).append("\n\n");
    }

    // Adding the column headers (A, B, C, etc.) to the StringBuilder
    public static void printColumnHeaders(int numOfColumns, int widthOfColumn, StringBuilder sb) {
        sb.append("     |");  //Leaving a space of 4 characters for line numbers
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
    // Adding each row, including the row number and cell values to the StringBuilder
    public static void printRow(int row, int numOfColumns, int widthOfColumn, Map<String, DTOCell> cells, StringBuilder sb) {
        sb.append(String.format("%4d", row)).append(" |"); //print the number of the row
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
    }




}
