package manager;

import dto.DTOCell;
import dto.DTOCoordinate;
import dto.DTOSheet;
import engine.Engine;
import engine.EngineImpl;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import jakarta.xml.bind.JAXBException;
import jaxb.schema.xmlprocessing.FileDataException;
import menu.Command;
import sheet.version.SheetVersionData;
import sheet.version.SheetVersionHandler;

/**
 * UIManagerImpl implements the UIManager interface, handling user interactions for managing the spreadsheet system.
 * This class processes user requests such as displaying and updating the sheet, loading and saving system state, and more.
 */
public class UIManagerImpl implements UIManager {

    private Engine engine; // The engine responsible for handling the core logic of the system
    private boolean isFirst; // Flag to determine if it's the first menu interaction


    /**
     * UIManagerImpl constructor
     */
    public UIManagerImpl() {
        this.engine = new EngineImpl();
        this.isFirst = true;
    }

    //*****************************************************************************************//

    /**
     * Handles for print the menu and get user choice.
     * @return the command corresponding to the user's choice.
     */
    @Override
    public Command printMenuAddGetUserChoice() {

        Scanner scanner = new Scanner(System.in);

        if(isFirstMenu()){
            printFirstMenu();
            return getChoiceFromFirstMenu();
        }
        else{
            printMainMenu();
            return getChoiceFromMainMenu();
        }

    }

    // Prints the initial menu options to the user
    private void printFirstMenu(){

        System.out.println("\n===============================");
        System.out.println("          Main Menu            ");
        System.out.println("===============================\n");
        System.out.printf("1." + "%s\n", Command.LOAD_XML_FILE.getDescription());
        System.out.printf("2." + "%s\n", Command.LOAD_SYSTEM_STATE.getDescription());
        System.out.printf("3." + "%s\n", Command.EXIT.getDescription());
    }
    // Get the user's selection from the first menu
    private Command getChoiceFromFirstMenu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select an option (1-3):");

        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        return Command.LOAD_XML_FILE;
                    case 2:
                        return Command.LOAD_SYSTEM_STATE;
                    case 3:
                        return Command.EXIT;
                    default:
                        System.out.println("The number you entered is out of range. ");
                        System.out.println("Please select an option between 1 and 3.");
                        printFirstMenu();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                printFirstMenu();
            }
        }

    }
    // Prints the main menu options to the user
    private void printMainMenu(){
        System.out.println("\n===============================");
        System.out.println("          Main Menu            ");
        System.out.println("===============================\n");

        int num =1;
        for (Command command : Command.values()) {
            System.out.printf(num+"."+"%s\n", command.getDescription());
            num++;
        }

        System.out.println("\n===============================");
        System.out.print("Please select an option (1-" + Command.values().length + "): ");
        System.out.println();
    }
    // Get the user's choice from the main menu
    private Command getChoiceFromMainMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice >= 1 && choice <= Command.values().length) {
                    return Command.values()[choice - 1];
                } else {
                    // טיפול במקרים שהמספר מחוץ לטווח האפשרויות
                    System.out.println("The number you entered is out of range. Please select an option between 1 and " + Command.values().length + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and " + Command.values().length + ".");
            }
        }
    }
    // Checks whether the initial menu should be shown to the user
    private boolean isFirstMenu(){
        return isFirst;
    }

    //*****************************************************************************************//

    /**
     * Handles the user's request to display the entire sheet.
     */

    @Override
    public void displaySheet(){
        DTOSheet dtoSheet = engine.displaySheet();
        printSheetToConsole(dtoSheet);
    }

    // Prints the entire sheet to the console
    private void printSheetToConsole(DTOSheet dtoSheet) {
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
    private void printSheetHeader(DTOSheet dtoSheet, StringBuilder sb) {
        sb.append("Sheet Title: ").append(dtoSheet.getSheetTitle()).append("\n")
                .append("Sheet Version: ").append(dtoSheet.getSheetVersion()).append("\n\n");
    }
    // Add the column headers (A, B, C, etc.) to the StringBuilder
    private void printColumnHeaders(int numOfColumns, int widthOfColumn, StringBuilder sb) {
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
    private void printRow(int row, int numOfColumns, int widthOfColumn, Map<String, DTOCell> cells, StringBuilder sb, int heightOfRow) {

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
    private void printEmptyRow(StringBuilder sb,int widthOfColumn, int numOfColumns ) {
        sb.append("    |");
        for (int col = 0; col < numOfColumns; col++) {
            sb.append(" ".repeat(widthOfColumn));
            if (col < numOfColumns - 1) {
                sb.append("|");
            }
        }
        sb.append("\n");  // Move to the next line after each row
    }

    //*****************************************************************************************//

    /**
     * Handles the user's request to load an XML file into the system.
     */

    @Override
    public void loadXmlFileFromUser() {
        String xmlFilePath = getXmlFileFullPath();
        loadXmlFile(xmlFilePath);
    }
    // Prompts the user to enter the full path to the XML file
    public String getXmlFileFullPath(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter the XML file full path: ");
        return scanner.nextLine();
    }
    // Loads an XML file from the specified path and handles errors
    public void loadXmlFile(String filePath) {

        int choice;

        try{
            // Attempt to load the sheet from the XML file
            engine.loadSheetFromXmlFile(filePath);
            // If successful, print a success message
            System.out.println("====================================");
            System.out.println("The XML file was loaded successfully.");
            System.out.println("====================================");

            isFirst=false;

        } catch (IllegalArgumentException e) {
            // Handle errors from isXmlFile (e.g., invalid file path or non-XML file)
            printErrorMessage();
            System.out.println(e.getMessage());
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }

        } catch (FileDataException.InvalidRowCountException e) {
            // Print a specific error message for invalid row count
            printErrorMessage();
            System.out.println("The XML file specifies an " + e.getMessage() + ".\nPlease ensure the sheet has between 1 and 50 rows.");
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }
        } catch (FileDataException.InvalidColumnCountException e) {
            // Print a specific error message for invalid column count
            printErrorMessage();
            System.out.println("The XML file specifies an " + e.getMessage() + ".\nPlease ensure the sheet has between 1 and 20 columns.");
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }
        } catch (FileDataException.InvalidColumnWidthException e) {
            // Print a specific error message for invalid column width
            printErrorMessage();
            System.out.println("The XML file specifies an " + e.getMessage() + ".\nPlease ensure the column width is a positive number.");
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }
        } catch (FileDataException.InvalidRowHeightException e) {
            // Print a specific error message for invalid row height
            printErrorMessage();
            System.out.println("The XML file specifies an " + e.getMessage() + ".\nPlease ensure the row height is a positive number.");
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }
        } catch (FileDataException.CellOutOfBoundsException e) {
            // Print a specific error message for cell out of bounds
            printErrorMessage();
            System.out.println("The file contains one or more cells that are positioned outside the valid sheet boundaries. \nPlease ensure all cells in the file are within the defined grid of the sheet.");
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }
        } catch (FileDataException.CircularReferenceException e) {
            // Print a specific error message for circular reference
            printErrorMessage();
            System.out.println("The file contains a circular reference, which is not allowed.");
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }
        } catch (FileNotFoundException e) {
            // Handle file not found
            printErrorMessage();
            System.out.println("The file was not found at the specified path: '" + filePath +"'.");
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }
        } catch (JAXBException e) {
            printErrorMessage();
            System.out.println(e.getMessage());
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }
        } catch (Exception e) {
            // Catch any other exceptions
            printErrorMessage();
            System.out.println("An unexpected error occurred: " + e.getMessage());
            choice = filePathErrorMenu();
            if(choice ==1){
                loadXmlFileFromUser();
            }
        }

    }
    // Displays the menu in case of an error loading the file
    private int filePathErrorMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===============================");
        System.out.println("1. Try loading the file again");
        System.out.println("2. Return to the main menu");
        System.out.println("===============================");
        System.out.println("Please select an option (1-2): ");
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    default:
                        System.out.println("Invalid choice. Please select either 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
            }
        }
    }

    //*****************************************************************************************//

    /**
     * Handles the user's request to display the details of a specific cell.
     */

    @Override
    public void displayCell() {
        try {
            // Step 1: Get cell identity from user
            String coordinateString = getCoordinateInput();
            DTOCell dtoCell = engine.displayCell(coordinateString);
            printCellDetails(dtoCell);

        }catch (Exception e) {
            printErrorDisplayCellMenu(e.getMessage());
        }

    }

    // Prints the details of the specified cell
    public void printCellDetails(DTOCell dtoCell) {
        System.out.println("***********************************************************");
        System.out.println("                      Cell Details                          ");
        System.out.println("***********************************************************");

        // Cell Identity
        DTOCoordinate coordinate = dtoCell.getCoordinate();
        System.out.println("Cell Coordinate: " + coordinate);

        // Original Value
        System.out.println("Original Value: " + dtoCell.getOriginalValue());

        // Effective Value
        System.out.println("Effective Value: " + dtoCell.getEffectiveValue());

        // Last Changed Version
        System.out.println("Last Changed Version: " + dtoCell.getLastModifiedVersion());

        // Direct Dependencies
        List<DTOCoordinate> dependencies = dtoCell.getDependsOn();
        System.out.println("Direct Dependencies: " + formatCoordinates(dependencies));

        // Direct Influences
        List<DTOCoordinate> influences = dtoCell.getInfluencingOn();
        System.out.println("Direct Influences: " + formatCoordinates(influences));

        System.out.println("***********************************************************");
    }
    // Formats the coordinates into a string for display
    private String formatCoordinates(List<DTOCoordinate> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return "None";
        }

        StringBuilder sb = new StringBuilder();
        for (DTOCoordinate coordinate : coordinates) {
            sb.append(coordinate)
                    .append(" ");
        }
        return sb.toString().trim();
    }
    // Handles errors that occur during cell display
    public void printErrorDisplayCellMenu(String errorMessage) {
        System.out.println("===========================================================");
        System.out.println("                     Error Displaying Cell                 ");
        System.out.println("===========================================================");
        System.out.println("An error occurred while trying to display the cell details:");
        System.out.println(errorMessage);
        System.out.println("Please choose one of the following options:");
        System.out.println("1. Try again");
        System.out.println("2. Return to the main menu");
        System.out.println("===========================================================");

        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.print("Enter your choice (1 or 2): ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 1) {
                    // Call the function that attempts to display cell details again
                    displayCell(); // Replace with the actual method to display cell details
                    break;
                } else if (choice == 2) {
                    System.out.println("Returning to the main menu...");
                    break;
                } else {
                    System.out.println("Invalid choice. Please select 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
            }
        }
    }

    //*****************************************************************************************//

    /**
     * Handles the user's request to update the value of a specific cell.
     */

    @Override
    public void updateCell() {

        try {
            String coordinate = getCoordinateInput();
            String originalValue = getNewOriginalValueInput();
            engine.updateCell(coordinate, originalValue);
            displaySheet();
        }catch (Exception e) {
            printErrorUpdateCellMenu(e.getMessage());
        }
    }

    // Prompts the user to enter the identity of the cell to update
    private String getCoordinateInput() {
        System.out.println("Please enter the identity of the cell you want to update.");
        System.out.println("The identity should consist of a column letter followed by a row number.");
        System.out.println("For example, if you want to update the cell in column A and row 4, enter 'A4'.");
        System.out.print("Enter the cell identity (e.g., A4): ");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        return input;
    }
    // Prompts the user to enter the new original value for the cell
    private String getNewOriginalValueInput() {
        System.out.print("Enter the new original value: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }
    // Handles errors that occur during cell update
    public void printErrorUpdateCellMenu(String errorMessage) {
        System.out.println("===========================================================");
        System.out.println("                     Error Updating Cell                   ");
        System.out.println("===========================================================");
        System.out.println("An error occurred while trying to update the cell:");// להשאיר??
        System.out.println(errorMessage);
        System.out.println("Please choose one of the following options:");
        System.out.println("1. Try again");
        System.out.println("2. Return to the main menu");
        System.out.println("===========================================================");

        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.print("Enter your choice (1 or 2): ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 1) {
                    updateCell(); // לקרוא לפונקציה שניסתה לעדכן תא
                    break;
                } else if (choice == 2) {
                    System.out.println("Returning to the main menu...");
                    break;
                } else {
                    System.out.println("Invalid choice. Please select 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
            }
        }
    }

    //*****************************************************************************************//

    /**
     * Handles the user's request to display a specific version of the sheet.
     */

    @Override
    public void displaySheetVersion() {
        int selectedVersion = getVersionNumberFromUser();
        while (selectedVersion == 0) {
            if(printErrorVersionMenu() ==1){
                selectedVersion =getVersionNumberFromUser();
            }
        }
        printSheetVersion(selectedVersion);
    }

    // Prompts the user to select a version number to display
    private int getVersionNumberFromUser() {
        Scanner scanner = new Scanner(System.in);
        SheetVersionHandler versionHandler = engine.getCurrentSheetState().getVersionHandler();
        int numOfVersion = versionHandler.getNumOfVersions();
        System.out.println("Please select a version number from the following list:");

        // Print version history
        printVersionsHistory();
        System.out.println();
        int selectedVersion =0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter your choice: ");
            try {
                selectedVersion = Integer.parseInt(scanner.nextLine());

                // בדיקה האם המספר נמצא בטווח התקין
                if (selectedVersion >= 1 && selectedVersion <= numOfVersion) {
                    validInput = true;
                } else {
                    System.out.println("Error: The number you entered is not in the list of available versions. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a number from the list.");
            }
        }
        return selectedVersion;

    }
    // Displays the error menu if the user selects an invalid version
    private int printErrorVersionMenu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("===============================\n");
        System.out.println("1. Try selecting a version number again");
        System.out.println("2. Return to the main menu");
        System.out.println("\n===============================");
        System.out.print("Please select an option (1-2): ");
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    default:
                        System.out.println("Invalid choice. Please select either 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
            }
        }
    }
    // Prints the specified version of the sheet
    private void printSheetVersion(int version) {
        SheetVersionHandler versionHandler = engine.getCurrentSheetState().getVersionHandler();
        DTOSheet dtoSheet = versionHandler.getSheetByVersion(version);
        printSheetToConsole(dtoSheet);
    }
    // Prints the history of all versions (num of in each version and in each one how many cells been updated
    private void printVersionsHistory() {
        SheetVersionHandler versionHandler = engine.getCurrentSheetState().getVersionHandler();

        StringBuilder sb = new StringBuilder();

        // Print header for version numbers
        sb.append("Version number:         |");
        for (SheetVersionData version : versionHandler.getVersionHistory()) {
            sb.append(" ").append(version.getDtoSheet().getSheetVersion()).append(" |");
        }
        sb.append("\n");

        // Print header for number of cell changes
        sb.append("Number of cell changes: |");
        for (SheetVersionData version : versionHandler.getVersionHistory()) {
            sb.append(" ").append(version.getNumOfUpdateCells()).append(" |");
        }
        sb.append("\n");

        // Print the result
        System.out.println(sb.toString());
    }

    //*****************************************************************************************//

    /**
     * Handles the user's request to load a previously saved system state from a file.
     */

    @Override
    public void loadSystemState() {
        String inputFilePath = getInputFilePathFromUser();
        int choice;
        try{
            engine.loadSystemState(inputFilePath);
            System.out.println("===============================");
            System.out.println("System state loaded successfully");
            System.out.println("===============================");
            isFirst=false;
        }
        catch (Exception e){
            printErrorMessage();
            System.out.println(e.getMessage());
            choice = loadSystemErrorMenu();
            if(choice ==1) {
                loadSystemState();
            }

        }
    }

    // Prompts the user to enter the full path to the input file
    private String getInputFilePathFromUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the full input file path: ");
        return scanner.nextLine();
    }

    private int loadSystemErrorMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===============================");
        System.out.println("1. Try load the system again");
        System.out.println("2. Return to the main menu");
        System.out.println("===============================");
        System.out.println("Please select an option (1-2): ");
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    default:
                        System.out.println("Invalid choice. Please select either 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
            }
        }
    }

    //*****************************************************************************************//

    /**
     * Handles the user's request to save the current system state to a file.
     */

    @Override
    public void saveSystemState() {
        String outputFilePath = getOutputFilePathFromUser();
        int choice;
        try{
            engine.saveSystemState(outputFilePath);
            System.out.println("===============================");
            System.out.println("System state saved successfully");
            System.out.println("===============================");

        }
        catch (Exception e) {
            printErrorMessage();
            System.out.println( e.getMessage());
            choice = saveSystemErrorMenu();//****************************************************************
            if (choice == 1) {
                saveSystemState();
            }
        }
    }

    // Prompts the user to enter the full path to the output file
    private String getOutputFilePathFromUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the full output file path: ");
        return scanner.nextLine();
    }



    private void printErrorMessage(){
        System.out.println("===============================");
        System.out.println("            Error:             ");
        System.out.println("===============================");

    }

    private int saveSystemErrorMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===============================");
        System.out.println("1. Try save the system again");
        System.out.println("2. Return to the main menu");
        System.out.println("===============================");
        System.out.println("Please select an option (1-2): ");
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    default:
                        System.out.println("Invalid choice. Please select either 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
            }
        }
    }

}
