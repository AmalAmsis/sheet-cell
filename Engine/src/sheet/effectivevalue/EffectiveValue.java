package sheet.effectivevalue;

public interface EffectiveValue {
    CellType getCellType();
    Object getValue();
    <T> T extractValueWithExpectation(Class<T> type);
}