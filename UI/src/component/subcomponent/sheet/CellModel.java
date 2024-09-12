package component.subcomponent.sheet;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CellModel {

    private final StringProperty value;
    private final ObjectProperty<Pos> alignment;
    private final ObjectProperty<Color> textColor;
    private final ObjectProperty<Color> backgroundColor;
    private final ObjectProperty<Font> font;


    public CellModel() {
        this.value = new SimpleStringProperty("");
        this.alignment = new SimpleObjectProperty<>(Pos.CENTER);
        this.textColor = new SimpleObjectProperty<>(Color.BLACK);
        this.backgroundColor = new SimpleObjectProperty<>(Color.WHITE);
        this.font = new SimpleObjectProperty<>(Font.getDefault());
    }

    public StringProperty valueProperty() {
        return value;
    }

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

    public void setValue(String value) {
        this.value.set(value);
    }

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

}
