
package component.subcomponent.sheet;

import component.main.app.AppController;

import dto.DTOCell;
import dto.DTOSheet;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.StyleClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import manager.UIManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SheetController {

    private AppController appController;
    private ObjectProperty<Label> selectedCell;

    @FXML
    private GridPane sheetGrid;

    //*******************************************************************************************************//
    private UIModelSheet uiModel;

    public SheetController() {
        this.uiModel = new UIModelSheet();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void initialize() {
        appController = new AppController();

        // לסדר את הקוד
        // selected cell listener
        selectedCell = new SimpleObjectProperty<>();
        selectedCell.addListener((observableValue, oldLabelSelection, newSelectedLabel) -> {
            if (oldLabelSelection != null) {
                int row = GridPane.getRowIndex(oldLabelSelection);
                int col = GridPane.getColumnIndex(oldLabelSelection);
                String oldCellId = getCellId(col, row);
                String cellId = getCellModelId(oldLabelSelection);
                appController.unShowCellData(cellId);
                uiModel.setCellBorderColor(oldCellId, CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue());
                uiModel.setCellBorderWidth(oldCellId, CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue());
                uiModel.setCellBorderStyle(oldCellId,CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue());
            }
            if (newSelectedLabel != null) {
                int row = GridPane.getRowIndex(newSelectedLabel);
                int col = GridPane.getColumnIndex(newSelectedLabel);
                String cellId = getCellId(col, row);
                uiModel.setCellBorderColor(cellId, CellStyle.SELECTED_CELL_BORDER_COLOR.getColorValue());
                uiModel.setCellBorderWidth(cellId, CellStyle.SELECTED_CELL_BORDER_WIDTH.getWidthValue());
                uiModel.setCellBorderStyle(cellId,CellStyle.SELECTED_CELL_BORDER_STYLE.getStyleValue());
            }
        });
    }

    public void initSheetAndBindToUIModel(DTOSheet dtoSheet) {

        sheetGrid.getChildren().clear();

        int numOfRows = dtoSheet.getNumOfRows();
        int numOfCols = dtoSheet.getNumOfColumns();
        int WidthOfCols = dtoSheet.getWidthOfColumns();
        int HeightOfRows = dtoSheet.getHeightOfRows();

        uiModel.initializeModel(numOfRows + 1, numOfCols + 1);

        for (int col = 1; col <= numOfCols; col++) {
            String colLetter = String.valueOf((char) ('A' + col - 1));
            String cellKey = getCellId(col, 0);
            Label cellLabel = new Label();
            cellLabel.setPrefSize(WidthOfCols, 15);
            uiModel.setCellValue(cellKey, colLetter);
            uiModel.bindCellToModel(cellLabel, cellKey);
            sheetGrid.add(cellLabel, col, 0);
        }

        for (int row = 1; row <= numOfRows; row++) {
            String cellKey = getCellId(0, row);
            Label cellLabel = new Label();
            cellLabel.setPrefSize(25, HeightOfRows);
            uiModel.setCellValue(cellKey, String.valueOf(row));
            uiModel.bindCellToModel(cellLabel, cellKey);
            sheetGrid.add(cellLabel, 0, row);
        }


        for (int row = 1; row <= numOfRows; row++) {
            for (int col = 1; col <= numOfCols; col++) {

                String cellKey = getCellId(col, row);
                Label cellLabel = new Label();
                cellLabel.setPrefSize(WidthOfCols, HeightOfRows);
                addClickEventForCell(cellLabel);


                DTOCell dtoCell = dtoSheet.getCells().get(cellKey);
                if (dtoCell != null) {
                    uiModel.setCellValue(cellKey, dtoCell.getEffectiveValue().toString());
                }

                uiModel.bindCellToModel(cellLabel, cellKey);
                sheetGrid.add(cellLabel, col, row);

            }
        }

    }



    private String getCellId(int col, int row) {
        char colLetter = (char) ('A' + (col - 1)); // ממיר מספר עמודה לאות, לדוגמה 1 -> A
        return String.valueOf(colLetter) + ":" + row;
    }

    //*******************************************************************************************************//

    private String getCellModelId(Label label){
        int rowIndex = GridPane.getRowIndex(label);
        int colIndex = GridPane.getColumnIndex(label);
        char colLetter = (char) ('A' + (colIndex - 1)); // ממיר מספר עמודה לאות, לדוגמה 1 -> A
        String cellId = String.valueOf(colLetter) + rowIndex;
        return cellId;
    }

    private void addClickEventForCell(Label label) {
        label.setOnMouseClicked(event -> {
            selectedCell.set(label);
            String cellId = getCellModelId(label);
            appController.showCellData(cellId);
        });
    }

    public void addBorderForCells(Color color, String style, double width, List<String> cellsId) {
        for (String cellId : cellsId)
        {
            uiModel.setCellBorderColor(cellId, color);
            uiModel.setCellBorderStyle(cellId,style);
            uiModel.setCellBorderWidth(cellId,width);
        }
    }

    public String getSelectedCellId(){
       return getCellModelId(selectedCell.get());
    }

    public void UpdateSheetValues(DTOSheet dtoSheet){
        Map<String,DTOCell> sheetMap = dtoSheet.getCells();
        for (DTOCell cell : sheetMap.values()) {
            uiModel.setCellValue(cell.getCoordinate().toString(), cell.getEffectiveValue().toString());
        }
    }

    public void setColumnWidth(int colIndex, double width) {
        for (Node node : sheetGrid.getChildren()) {
            if (GridPane.getColumnIndex(node) == colIndex) {
                ((Label) node).setPrefWidth(width);
            }
        }
    }

    public void setRowHeight(int rowIndex, double height) {
        for (Node node : sheetGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == rowIndex) {
                ((Label) node).setPrefHeight(height);
            }
        }
    }


    public UIModelSheet getCurrentUIModel() {
        return uiModel;
    }

}



//        public void diaplaySheet1 (DTOSheet dtoSheet){
//
//            int numOfRows = dtoSheet.getNumOfRows();
//            int numOfCols = dtoSheet.getNumOfColumns();
//            int WidthOfCols = dtoSheet.getWidthOfColumns();
//            int HeightOfRows = dtoSheet.getHeightOfRows();
//
//            // Set the top row to a fixed height of 30 pixels for column headers (A, B, C...)
//            sheetGrid.getRowConstraints().add(createRowConstraint(15, true));
//
//            // Set the leftmost column to a fixed width of 30 pixels for row numbers (1, 2, 3...)
//            sheetGrid.getColumnConstraints().add(createColumnConstraint(15, true));
//
//            // Set the rest of the rows (height can be flexible or fixed based on the need)
//            for (int col = 1; col <= numOfCols; col++) {
//                sheetGrid.getColumnConstraints().add(createColumnConstraint(WidthOfCols, true));
//            }
//
//            // Set the rest of the columns (width can be flexible or fixed based on the need)
//            for (int row = 1; row <= numOfRows; row++) {
//                sheetGrid.getRowConstraints().add(createRowConstraint(HeightOfRows, true));
//            }
//
//            // Add headers to the top row (A, B, C...)
//            for (int col = 1; col <= numOfCols; col++) {
//                String colLetter = String.valueOf((char) ('A' + col - 1));
//                Label label = new Label(colLetter);
//                label.setStyle("-fx-font-weight: bold;");
//                StackPane headerPane = new StackPane(label); // Wrap label inside StackPane
//                headerPane.setAlignment(javafx.geometry.Pos.CENTER); // Center the column header text
//                sheetGrid.add(headerPane, col, 0);
//            }
//
//
//            // Add numbers to the leftmost column (1, 2, 3...)
//            for (int row = 1; row <= numOfRows; row++) {
//                Label label = new Label(String.valueOf(row));
//                label.setStyle("-fx-font-weight: bold;");
//                StackPane numberPane = new StackPane(label); // Wrap label inside StackPane
//                numberPane.setAlignment(javafx.geometry.Pos.CENTER); // Center the row number text
//                sheetGrid.add(numberPane, 0, row);
//            }
//
//            // Add cells from the sheet's map
//            Map<String, DTOCell> board = dtoSheet.getCells();
//            for (int row = 1; row <= numOfRows; row++) {
//                for (int col = 1; col <= numOfCols; col++) {
//                    String cellKey = getCellId(col, row); // Create key like "A:1", "B:2", etc.
//
//                    // Check if the cell exists in the map
//                    DTOCell dtoCell = board.get(cellKey);
//
//                    try {
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/subcomponent/cell/cell.fxml"));
//                        StackPane cellPane = loader.load();
//
//                        Label cellLabel = (Label) cellPane.lookup("#cellLabel");
//
//                        if (dtoCell != null) {
//                            // If the cell exists in the map, display its effective value
//                            cellLabel.setText(dtoCell.getEffectiveValue().toString());
//                        } else {
//                            // If the cell doesn't exist, create an empty cell
//                            cellLabel.setText(""); // Empty cell
//                        }
//
//                        // Center the text in the cell
//                        cellLabel.setAlignment(javafx.geometry.Pos.CENTER); // Center the content in the label
//
//
//                        // Add the cell to the GridPane (excluding the top row and leftmost column)
//                        sheetGrid.add(cellPane, col, row);
//
//                    } catch (IOException e) {
//                        System.out.println("Error loading cell.fxml for: " + cellKey);
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        private javafx.scene.layout.ColumnConstraints createColumnConstraint ( double preferredWidth, boolean isFixedWidth){
//            javafx.scene.layout.ColumnConstraints column = new javafx.scene.layout.ColumnConstraints();
//
//            if (isFixedWidth) {
//                // במצב רוחב קבוע
//                column.setMinWidth(preferredWidth);
//                column.setPrefWidth(preferredWidth);
//                column.setMaxWidth(preferredWidth);
//            } else {
//                // במצב רוחב ניתן לשינוי
//                column.setMinWidth(preferredWidth);
//                column.setPrefWidth(preferredWidth);
//                column.setMaxWidth(Double.MAX_VALUE); // אין מגבלה על הרוחב
//                column.setHgrow(javafx.scene.layout.Priority.ALWAYS); // העמודה יכולה לגדול עם הגודל של החלון
//            }
//
//            return column;
//        }
//
//        // פונקציה ליצירת RowConstraints
//        private javafx.scene.layout.RowConstraints createRowConstraint ( double preferredHeight, boolean isFixedHeight){
//            javafx.scene.layout.RowConstraints row = new javafx.scene.layout.RowConstraints();
//
//            if (isFixedHeight) {
//                // במצב גובה קבוע
//                row.setMinHeight(preferredHeight);
//                row.setPrefHeight(preferredHeight);
//                row.setMaxHeight(preferredHeight);
//            } else {
//                // במצב גובה ניתן לשינוי
//                row.setMinHeight(preferredHeight);
//                row.setPrefHeight(preferredHeight);
//                row.setMaxHeight(Double.MAX_VALUE); // אין מגבלה על הגובה
//                row.setVgrow(javafx.scene.layout.Priority.ALWAYS); // השורה יכולה לגדול עם הגודל של החלון
//            }
//
//            return row;
//        }






