package component.selectedSheetView.subcomponent.sheet;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class CellModel {

    private final StringProperty value;
    private final StringProperty originalValue;
    private final IntegerProperty lastModifiedVersion;
    private final ObjectProperty<Pos> alignment;
    private final ObjectProperty<Color> textColor;
    private final ObjectProperty<Color> backgroundColor;
    private final ObjectProperty<Font> font;
    private final ObjectProperty<Color> borderColor;
    private final StringProperty borderStyle;
    private final DoubleProperty borderWidth;
    private final IntegerProperty Hight;
    private final IntegerProperty Width;
    private final ObjectProperty<List<String>> DependsOn;
    private final ObjectProperty<List<String>> InfluencingOn;




    public CellModel(int hight, int width) {
        this.value = new SimpleStringProperty("");
        this.originalValue = new SimpleStringProperty("");
        this.lastModifiedVersion = new SimpleIntegerProperty(0);
        this.alignment = new SimpleObjectProperty<>(Pos.CENTER);
        this.textColor = new SimpleObjectProperty<>(Color.BLACK);
        this.backgroundColor = new SimpleObjectProperty<>(CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue());
        this.font = new SimpleObjectProperty<>(Font.getDefault());
        this.borderColor = new SimpleObjectProperty<>(CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue());
        this.borderStyle = new SimpleStringProperty(CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue());
        this.borderWidth = new SimpleDoubleProperty(CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue());
        this.Hight = new SimpleIntegerProperty(hight);
        this.Width = new SimpleIntegerProperty(width);
        this.DependsOn = new SimpleObjectProperty<>(new ArrayList<>());
        this.InfluencingOn = new SimpleObjectProperty<>(new ArrayList<>());
    }

    public StringProperty valueProperty() {
        return value;
    }

    public StringProperty originalValueProperty() {return originalValue;}

    public IntegerProperty lastModifiedVersionProperty() {return lastModifiedVersion;}

    public ObjectProperty<Pos> alignmentProperty() {
        return alignment;
    }

    public ObjectProperty<Color> textColorProperty() {
        return textColor;
    }

    public ObjectProperty<Color> backgroundColorProperty() {
        return backgroundColor;
    }

    public ObjectProperty<Font> fontProperty() {
        return font;
    }

    public ObjectProperty<Color> borderColorProperty() {
        return borderColor;
    }

    public StringProperty borderStyleProperty() {
        return borderStyle;
    }

    public DoubleProperty borderWidthProperty() {
        return borderWidth;
    }

    public IntegerProperty heightProperty() {
        return Hight;
    }

    public IntegerProperty widthProperty() {
        return Width;
    }

    public ObjectProperty<List<String>> DependsOnProperty() {return DependsOn;}

    public ObjectProperty<List<String>> InfluencingOnProperty() {return InfluencingOn;}


    public void setValue(String value) {
        this.value.set(value);
    }

    public void setOriginalValue(String value) {this.originalValue.set(value);}

    public void setLastModifiedVersion(int version) {this.lastModifiedVersion.set(version);}

    public void setAlignment(Pos alignment) {
        this.alignment.set(alignment);
    }

    public void setTextColor(Color color) {
        this.textColor.set(color);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor.set(color);
    }

    public void setFont(Font font) {
        this.font.set(font);
    }

    public void setBorderColor(Color color) {
        this.borderColor.set(color);
    }

    public void setBorderStyle(String style) {
        this.borderStyle.set(style);
    }

    public void setBorderWidth(double width) {
        this.borderWidth.set(width);
    }

    public void setHeight(Integer hight) {
        this.Hight.set(hight);
    }

    public void setWidth(Integer width) {
        this.Width.set(width);
    }

    public void setDependsOn(List<String> dependsOn) {this.DependsOn.set(dependsOn); }

    public void setInfluencingOn(List<String> InfluencingOn) {this.InfluencingOn.set(InfluencingOn); }

}
