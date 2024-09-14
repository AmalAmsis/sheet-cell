package component.subcomponent.sheet;

import javafx.scene.paint.Color;

public enum CellStyle {

    // תא רגיל
    NORMAL_CELL_BORDER_COLOR(Color.BLACK), // צבע המסגרת של תא רגיל
    NORMAL_CELL_BORDER_STYLE("solid"),     // סגנון המסגרת של תא רגיל
    NORMAL_CELL_BORDER_WIDTH(2.0),         // רוחב המסגרת של תא רגיל
    NORMAL_CELL_BACKGROUND_COLOR(Color.WHITE), // צבע הרקע של תא רגיל


    SELECTED_CELL_BORDER_COLOR(Color.PINK),
    SELECTED_CELL_BORDER_STYLE("solid"),
    SELECTED_CELL_BORDER_WIDTH(3.0),

    DEPENDS_ON_CELL_BORDER_COLOR(Color.LIGHTBLUE),
    DEPENDS_ON_CELL_BORDER_STYLE("dashed"),
    DEPENDS_ON_CELL_BORDER_WIDTH(3.0),

    INFLUENCING_ON_CELL_BORDER_COLOR(Color.LIGHTGREEN),
    INFLUENCING_ON_CELL_BORDER_STYLE("dashed"),
    INFLUENCING_ON_CELL_BORDER_WIDTH(3.0),

    RANGE_CELL_BORDER_COLOR(Color.PURPLE),
    RANGE_CELL_BORDER_STYLE("dashed"),
    RANGE_CELL_BORDER_WIDTH(3.0);




    private Color colorValue;
    private String styleValue;
    private Double widthValue;

    // קונסטרקטור עבור צבע (Color)
    CellStyle(Color colorValue) {
        this.colorValue = colorValue;
    }

    // קונסטרקטור עבור סטייל (String)
    CellStyle(String styleValue) {
        this.styleValue = styleValue;
    }

    // קונסטרקטור עבור רוחב (double)
    CellStyle(Double widthValue) {
        this.widthValue = widthValue;
    }

    // Getters
    public Color getColorValue() {
        return colorValue;
    }

    public String getStyleValue() {
        return styleValue;
    }

    public Double getWidthValue() {
        return widthValue;
    }
}
