package sheet;

import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;

public interface SheetDataRetriever {
    int getVersion();
    String getTitle();
    EffectiveValue getCellEffectiveValue(Coordinate coordinate);
    public boolean isCellInSheet(Coordinate coordinate);
}
