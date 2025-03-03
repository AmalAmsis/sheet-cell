package sheetmanager.sheet.effectivevalue;

import java.io.Serializable;
import java.text.DecimalFormat;

public class EffectiveValueImpl implements EffectiveValue, Serializable {
    private CellType cellType;
    private Object value;

    public EffectiveValueImpl(){};

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
        throw new IllegalArgumentException("Expected type " + type.getName() + " but found " + cellType.getType().getName());
    }

    @Override
    public EffectiveValue createDeepCopy() {
        // We can directly copy cellType since it's an enum (immutable)
        CellType newCellType = this.cellType;

        // For the value, check its type and handle accordingly
        Object newValue;
        if (this.value instanceof String) {
            newValue = new String((String) this.value);  // Create a new String object
        } else if (this.value instanceof Number) {
            newValue = this.value; // Numbers are immutable, safe to copy the reference
        } else {
            // If value is another type of object, implement the logic for deep copying it
            // In this case, we'll assume value is either String or Number
            newValue = this.value; // Default to shallow copy for unsupported types
        }

        // Return a new EffectiveValueImpl object with copied values
        return new EffectiveValueImpl(newCellType, newValue);
    }



    @Override
    public String toString() {
        if (cellType == CellType.NUMERIC) {
            if ( value.equals("NaN")) {
                return "NaN";
            }
            else {
                DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                return decimalFormat.format(value);
            }
        }

        if (cellType == CellType.EMPTY) {
            //return "Empty Cell";
            return "";
        }

        if (cellType == CellType.BOOLEAN){
            if ( value.equals("UNKNOWN")) {
                return "UNKNOWN";
            }
            return Boolean.toString((Boolean) value).toUpperCase();
        }
        return this.extractValueWithExpectation(cellType.getType()).toString();
    }


}
