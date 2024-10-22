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

        if (value.getCellType() == CellType.STRING) {
            out.name("value").value(value.getValue().toString());
        } else if (value.getCellType() == CellType.BOOLEAN) {
            // במקרה של בוליאני או ערך שאינו בוליאני כמו "UNKNOWN"
            if ("UNKNOWN".equals(value.getValue())) {
                out.name("value").value("UNKNOWN");
            } else {
                out.name("value").value((Boolean) value.getValue());
            }
        } else if (value.getCellType() == CellType.NUMERIC && value.getValue() instanceof Double) {
            Double numericValue = (Double) value.getValue();
            if (numericValue.isNaN()) {
                out.name("value").value("NaN");
            } else {
                out.name("value").value(numericValue);
            }
        } else if (value.getCellType() == CellType.EMPTY) {
            out.name("value").value(""); // ייצוג של תא ריק כמחרוזת ריקה
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
                    if (cellType == CellType.NUMERIC) {
                        String numValue = in.nextString();
                        if ("NaN".equals(numValue)) {
                            value = Double.NaN;
                        } else {
                            value = Double.valueOf(numValue);
                        }
                    } else if (cellType == CellType.STRING) {
                        value = in.nextString();
                    } else if (cellType == CellType.BOOLEAN) {
                        String booleanValue = in.nextString();
                        if ("UNKNOWN".equals(booleanValue)) {
                            value = "UNKNOWN";
                        } else {
                            value = Boolean.valueOf(booleanValue);
                        }
                    } else if (cellType == CellType.EMPTY) {
                        value = ""; // תא ריק
                    }
                    break;
            }
        }
        in.endObject();

        return new EffectiveValueImpl(cellType, value);
    }
}
