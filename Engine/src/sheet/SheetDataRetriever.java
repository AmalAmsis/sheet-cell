package sheet;

import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;

public interface SheetDataRetriever {
    int getVersion();
    String getTitle();
    EffectiveValue getCellEffectiveValue(Coordinate coordinate);
    public boolean isCellInSheet(Coordinate coordinate);
    int getHeightOfRows();
    int getWidthOfCols();
    int getNumOfRows();
    int getNumOfCols();

    // Adding a new method to track dependencies
    void addDependentCell(Coordinate mainCell, Coordinate dependentCell);
}
