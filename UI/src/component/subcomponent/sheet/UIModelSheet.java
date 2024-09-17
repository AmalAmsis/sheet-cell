package component.subcomponent.sheet;

import javafx.css.StyleClass;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class UIModelSheet {

    private final Map<String, CellModel> cells;
    private int numberOfRows;
    private int numberOfColumns;
    private int cellWidth;
    private int cellHeight;

    public UIModelSheet() {
        cells = new HashMap<>();
    }

    public void initializeModel(int numOfRows, int numOfCols, int cellWidth, int cellHeight) {

        this.numberOfRows = numOfRows;
        this.numberOfColumns = numOfCols;
        cells.clear();
        for (int row = 0; row <= numOfRows; row++) {
            for (int col = 0; col <= numOfCols; col++) {
                String cellKey = getCellId(col, row);
                cells.put(cellKey, new CellModel(cellHeight, cellWidth));
            }
        }

        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    public CellModel getCell(String cellId) {
        return cells.get(cellId);
    }

    public Color getCellTextColor(String cellId) {
        return cells.get(cellId).textColorProperty().getValue();
    }

    public Color getCellBackgroundColor(String cellId) {
        return cells.get(cellId).backgroundColorProperty().getValue();
    }

    public int getCellHeight(String cellId) {
        return  cells.get(cellId).hightProperty().getValue();
    }
    public int getCellWidth(String cellId) {
        return  cells.get(cellId).widthProperty().getValue();
    }

    public void setCellValue(String cellId, String value) {
        cells.get(cellId).setValue(value);
    }

    public void setCellAlignment(String cellId, Pos alignment) {
        cells.get(cellId).setAlignment(alignment);
    }

    public void setCellTextColor(String cellId, Color color) {
        cells.get(cellId).setTextColor(color);
    }

    public void setCellBackgroundColor(String cellId, Color color) {
        cells.get(cellId).setBackgroundColor(color);
    }

    public void setCellFont(String cellId, Font font) {
        cells.get(cellId).setFont(font);
    }

    public void setCellWidth(String cellId, int width) {
        cells.get(cellId).setWidth(width);
    }
    public void setCellHeight(String cellId, int height) {
        cells.get(cellId).setHight(height);
    }



    //Amal
    public void setCellBorderColor(String cellId, Color color) {cells.get(cellId).setBorderColor(color);}

    public void setCellBorderWidth(String cellId, double width) {cells.get(cellId).setBorderWidth(width);}

    public void setCellBorderStyle(String cellId, String style) {cells.get(cellId).setBorderStyle(style);}


    private String getCellId(int col, int row) {
        char colLetter = (char) ('A' + (col - 1)); // ממיר מספר עמודה לאות, לדוגמה 1 -> A
        return String.valueOf(colLetter) + ":" + row;
    }


    public void bindCellToModel(Label label, String cellId) {
        CellModel cell = getCell(cellId);

        label.textProperty().bind(cell.valueProperty());
        label.alignmentProperty().bind(cell.alignmentProperty());
        label.textFillProperty().bind(cell.textColorProperty());
        label.fontProperty().bind(cell.fontProperty());
        label.prefWidthProperty().bind(cell.widthProperty());
        label.maxWidthProperty().bind(cell.widthProperty());
        label.minWidthProperty().bind(cell.widthProperty());
        label.prefHeightProperty().bind(cell.hightProperty());
        label.maxHeightProperty().bind(cell.hightProperty());
        label.minHeightProperty().bind(cell.hightProperty());



        // מחילים את הסגנון הראשוני
        updateLabelStyle(label, cell);

        // מאזינים לשינויים במאפיינים וקוראים לעדכון הסגנון
        cell.backgroundColorProperty().addListener((obs, oldColor, newColor) -> updateLabelStyle(label, cell));
        cell.borderColorProperty().addListener((obs, oldColor, newColor) -> updateLabelStyle(label, cell));
        cell.borderWidthProperty().addListener((obs, oldWidth, newWidth) -> updateLabelStyle(label, cell));
        cell.borderStyleProperty().addListener((obs, oldStyle, newStyle) -> updateLabelStyle(label, cell));





    }

    private String toRgbString(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return "rgb(" + r + "," + g + "," + b + ")";
    }

    private void updateLabelStyle(Label label, CellModel cell) {
        String style = String.format(
                "-fx-background-color: %s; -fx-border-color: %s; -fx-border-width: %spx; -fx-border-style: %s;",
                toRgbString(cell.backgroundColorProperty().get()),
                toRgbString(cell.borderColorProperty().get()),
                cell.borderWidthProperty().get(),
                cell.borderStyleProperty().get()
        );
        label.setStyle(style);
    }

    public void setColumnWidth(int colIndex, int width) {
        for (int row =0;row<numberOfRows; row++){
            String cellId = getCellId(colIndex, row);
            cells.get(cellId).setWidth(width);
        }
    }

    public void setRowHeight(int rowIndex, int height) {
        for (int col = 0; col<numberOfColumns; col++){
            String cellId = getCellId(col, rowIndex);
            cells.get(cellId).setHight(height);
        }
    }


    public void setColumnAlignment(int colIndex, Pos alignment) {
        for (int row = 1; row<numberOfRows; row++){
            String cellId = getCellId(colIndex, row);
            cells.get(cellId).setAlignment(alignment);
        }
    }

    public Pos getCellAlignment(String cellId) {
        return cells.get(cellId).alignmentProperty().getValue();
    }
    public UIModelSheet copyModel() {
        UIModelSheet newModel = new UIModelSheet();
        copyCellsTo(newModel);
        return newModel;
    }

    // פונקציה שמעתיקה את התאים ממודל אחד למודל אחר
    protected void copyCellsTo(UIModelSheet newModel) {
        for (Map.Entry<String, CellModel> entry : this.cells.entrySet()) {
            String cellId = entry.getKey();
            CellModel originalCell = entry.getValue();

            CellModel newCell = new CellModel(cellWidth, cellHeight);
            newCell.setValue(originalCell.valueProperty().get());
            newCell.setAlignment(originalCell.alignmentProperty().get());
            newCell.setTextColor(originalCell.textColorProperty().get());
            newCell.setBackgroundColor(originalCell.backgroundColorProperty().get());
            newCell.setFont(originalCell.fontProperty().get());
            newCell.setBorderColor(originalCell.borderColorProperty().get());
            newCell.setBorderWidth(originalCell.borderWidthProperty().get());
            newCell.setBorderStyle(originalCell.borderStyleProperty().get());
            newCell.setHight(originalCell.hightProperty().get());
            newCell.setWidth(originalCell.widthProperty().get());
            newCell.setAlignment(originalCell.alignmentProperty().get());

            newModel.cells.put(cellId, newCell);
        }
    }



}
