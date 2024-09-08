package subcomponents.sheet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sheet.SheetImpl;
import sheet.cell.Cell;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;
import subcomponents.cell.CellController;

import java.io.IOException;
import java.util.Map;

public class SheetController {

    @FXML
    private GridPane sheetGrid;

    public void loadSheet(SheetImpl sheet) {

        int numOfRows = sheet.getNumOfRows();
        int numOfCols = sheet.getNumOfCols();
        int WidthOfCols = sheet.getWidthOfCols();
        int HeightOfRows = sheet.getHeightOfRows();

        // Set the top row to a fixed height of 30 pixels for column headers (A, B, C...)
        sheetGrid.getRowConstraints().add(createRowConstraint(15, true));

        // Set the leftmost column to a fixed width of 30 pixels for row numbers (1, 2, 3...)
        sheetGrid.getColumnConstraints().add(createColumnConstraint(15, true));

        // Set the rest of the rows (height can be flexible or fixed based on the need)
        for (int col = 1; col <= numOfCols; col++) {
            sheetGrid.getColumnConstraints().add(createColumnConstraint(WidthOfCols, true));
        }

        // Set the rest of the columns (width can be flexible or fixed based on the need)
        for (int row = 1; row <= numOfRows; row++) {
            sheetGrid.getRowConstraints().add(createRowConstraint(HeightOfRows, true));
        }

        // Add headers to the top row (A, B, C...)
        for (int col = 1; col <= numOfCols; col++) {
            String colLetter = String.valueOf((char) ('A' + col - 1));
            Label label = new Label(colLetter);
            label.setStyle("-fx-font-weight: bold;");
            StackPane headerPane = new StackPane(label); // Wrap label inside StackPane
            headerPane.setAlignment(javafx.geometry.Pos.CENTER); // Center the column header text
            sheetGrid.add(headerPane, col, 0);
        }


        // Add numbers to the leftmost column (1, 2, 3...)
        for (int row = 1; row <= numOfRows; row++) {
            Label label = new Label(String.valueOf(row));
            label.setStyle("-fx-font-weight: bold;");
            StackPane numberPane = new StackPane(label); // Wrap label inside StackPane
            numberPane.setAlignment(javafx.geometry.Pos.CENTER); // Center the row number text
            sheetGrid.add(numberPane, 0, row);
        }

        // Add cells from the sheet's map
        Map<String, Cell> board = sheet.getBoard();
        for (int row = 1; row <= numOfRows; row++) {
            for (int col = 1; col <= numOfCols; col++) {
                String cellKey = createCellKey(col, row); // Create key like "A:1", "B:2", etc.

                // Check if the cell exists in the map
                Cell cell = board.get(cellKey);

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/subcomponents/cell/cell.fxml"));
                    StackPane cellPane = loader.load();

                    Label cellLabel = (Label) cellPane.lookup("#cellLabel");

                    if (cell != null) {
                        // If the cell exists in the map, display its effective value
                        cellLabel.setText(cell.getEffectiveValue().toString());
                    } else {
                        // If the cell doesn't exist, create an empty cell
                        cellLabel.setText(""); // Empty cell
                    }

                    // Center the text in the cell
                    cellLabel.setAlignment(javafx.geometry.Pos.CENTER); // Center the content in the label


                    // Add the cell to the GridPane (excluding the top row and leftmost column)
                    sheetGrid.add(cellPane, col, row);

                } catch (IOException e) {
                    System.out.println("Error loading cell.fxml for: " + cellKey);
                    e.printStackTrace();
                }
            }
        }
    }


    // פונקציה ליצירת ColumnConstraints
    private javafx.scene.layout.ColumnConstraints createColumnConstraint(double preferredWidth, boolean isFixedWidth) {
        javafx.scene.layout.ColumnConstraints column = new javafx.scene.layout.ColumnConstraints();

        if (isFixedWidth) {
            // במצב רוחב קבוע
            column.setMinWidth(preferredWidth);
            column.setPrefWidth(preferredWidth);
            column.setMaxWidth(preferredWidth);
        } else {
            // במצב רוחב ניתן לשינוי
            column.setMinWidth(preferredWidth);
            column.setPrefWidth(preferredWidth);
            column.setMaxWidth(Double.MAX_VALUE); // אין מגבלה על הרוחב
            column.setHgrow(javafx.scene.layout.Priority.ALWAYS); // העמודה יכולה לגדול עם הגודל של החלון
        }

        return column;
    }

    // פונקציה ליצירת RowConstraints
    private javafx.scene.layout.RowConstraints createRowConstraint(double preferredHeight, boolean isFixedHeight) {
        javafx.scene.layout.RowConstraints row = new javafx.scene.layout.RowConstraints();

        if (isFixedHeight) {
            // במצב גובה קבוע
            row.setMinHeight(preferredHeight);
            row.setPrefHeight(preferredHeight);
            row.setMaxHeight(preferredHeight);
        } else {
            // במצב גובה ניתן לשינוי
            row.setMinHeight(preferredHeight);
            row.setPrefHeight(preferredHeight);
            row.setMaxHeight(Double.MAX_VALUE); // אין מגבלה על הגובה
            row.setVgrow(javafx.scene.layout.Priority.ALWAYS); // השורה יכולה לגדול עם הגודל של החלון
        }

        return row;
    }

    private String createCellKey(int col, int row) {
        char colLetter = (char) ('A' + (col - 1)); // ממיר מספר עמודה לאות, לדוגמה 1 -> A
        return String.valueOf(colLetter) + ":" + row;
    }


    public void initialize() {
        sheetGrid.setGridLinesVisible(true);
    }
}