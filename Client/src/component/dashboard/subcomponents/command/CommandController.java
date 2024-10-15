package component.dashboard.subcomponents.command;

import JsonSerializer.JsonSerializer;
import component.dashboard.main.maindashboard.DashboardController;
import component.popup.error.ErrorMessage;
import dto.DTOCell;
import dto.DTOSheet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import util.http.HttpClientUtil;



import java.io.IOException;
import java.util.Map;

import javafx.event.ActionEvent;

import static util.Constants.VIEW;

public class CommandController {

    private DashboardController dashboardController;

    @FXML private Button ackOrDenyPermissionRequestButton;
    @FXML private Button requestPermissionButton;
    @FXML private Button viewSheetButton;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }


    public DashboardController getDashboardController() {
        return dashboardController;
    }


    @FXML void viewSheetHandler(ActionEvent event) {

        String fileName = dashboardController.getSelectedSheetName();
        String username = "lo_user";  // שם המשתמש הקבוע

        if(fileName != null) {
            String url = VIEW + "?username=" + username + "&sheetName=" + fileName;

            // יצירת בקשת GET
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            // שליחת הבקשה
            Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

            try {
                Response response = call.execute();

                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();

                    JsonSerializer jsonSerializer = new JsonSerializer();
                    DTOSheet dtoSheet = jsonSerializer.convertJsonToDto(jsonResponse);

                    dashboardController.switchToSelectedSheetView(dtoSheet);
                    printSheetToConsole(dtoSheet);
                } else {
                    new ErrorMessage("Failed to fetch sheet: " + response.code());
                }

            } catch (IOException e) {
                new ErrorMessage("Error fetching sheet: " + e.getMessage());
            }
        } else {
            new ErrorMessage("No sheet selected.");
        }  }


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

}
