package JsonSerializer;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;

import java.io.IOException;

public class EffectiveValueAdapter extends TypeAdapter<EffectiveValue> {

    @Override
    public void write(JsonWriter out, EffectiveValue value) throws IOException {
        out.beginObject();
        out.name("cellType").value(value.getCellType().toString());

        if (value.getCellType() == CellType.STRING || value.getCellType() == CellType.BOOLEAN) {
            out.name("value").value(value.getValue().toString());
        } else if (value.getCellType() == CellType.NUMERIC && value.getValue() instanceof Number) {
            out.name("value").value((Number) value.getValue());
        } else if (value.getCellType() == CellType.EMPTY) {
            out.name("value").value(""); // Represent empty cell as an empty string
        }
        out.endObject();
    }
    @Override
    public EffectiveValue read(JsonReader in) throws IOException {
        in.beginObject();
        CellType cellType = null;
        Object value = null;

        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "cellType":
                    cellType = CellType.valueOf(in.nextString());
                    break;
                case "value":
                    // קריאה בהתאם לסוג ה-CellType
                    if (cellType == CellType.NUMERIC) {
                        value = in.nextDouble();
                    } else if (cellType == CellType.STRING || cellType == CellType.BOOLEAN) {
                        value = in.nextString();
                    } else if (cellType == CellType.EMPTY) {
                        value = ""; // תא ריק, לכן נשתמש ב-null
                    }
                    break;
            }
        }
        in.endObject();

        // יצירת אובייקט EffectiveValue מתאים
        return new EffectiveValueImpl(cellType, value);
    }

}
