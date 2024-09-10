package component.subcomponent.sheet;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class UIModelSheet {

    private final Map<String, CellModel> cells;

    public UIModelSheet() {
        cells = new HashMap<>();
    }


    public void initializeModel(int numOfRows, int numOfCols) {

        cells.clear();
        for (int row = 0; row <= numOfRows; row++) {
            for (int col = 0; col <= numOfCols; col++) {
                String cellKey = getCellId(col, row);
                cells.put(cellKey, new CellModel());
            }
        }
    }

    public CellModel getCell(String cellId) {
        return cells.get(cellId);
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

        cell.backgroundColorProperty().addListener((obs, oldColor, newColor) -> {
            label.setStyle("-fx-background-color: " + toRgbString(newColor) + ";");
        });

        label.setStyle("-fx-background-color: " + toRgbString(cell.backgroundColorProperty().get()) + ";");

    }

    private String toRgbString(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return "rgb(" + r + "," + g + "," + b + ")";
    }


}
