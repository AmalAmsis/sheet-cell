package sheet.effectivevalue;

import java.io.Serializable;
import java.text.DecimalFormat;

public class EffectiveValueImpl implements EffectiveValue, Serializable {
    private CellType cellType;
    private Object value;

    public EffectiveValueImpl(CellType cellType, Object value) {
        this.cellType = cellType;
        this.value = value;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public <T> T extractValueWithExpectation(Class<T> type) {
        if (cellType.isAssignableFrom(type)) {
            return type.cast(value);
        }
        // error handling... exception ? return null ?
        return null;
    }


    @Override
    public String toString() {
        if (cellType == CellType.NUMERIC && value instanceof Number) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            return decimalFormat.format(value);
        }
        if (cellType == CellType.EMPTY) {
            //return "Empty Cell";
            return "";
        }
        return this.extractValueWithExpectation(cellType.getType()).toString();
    }


}
