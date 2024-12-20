package sheetmanager.sheet.effectivevalue;

public enum CellType {
    NUMERIC(Double.class) ,
    STRING(String.class) ,
    BOOLEAN(Boolean.class) ,
    EMPTY(Void.class);

    private Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

    public boolean isAssignableFrom(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }

    public Class<?> getType() {
        return type;
    }
}