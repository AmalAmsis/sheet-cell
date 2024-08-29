package sheet;

import sheet.cell.Cell;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;

import java.util.Map;

public interface SheetDataRetriever {
    int getVersion();
    String getTitle();
    EffectiveValue getCellEffectiveValue(Coordinate coordinate);
    public boolean isCellInSheet(Coordinate coordinate);
    int getHeightOfRows();
    int getWidthOfCols();
    int getNumOfRows();
    int getNumOfCols();
    public Map<String, Cell> getBoard();
    Coordinate convertStringToCoordinate(String stringCoordinate);

    // Adding a new method to track dependencies
    void addDependentCell(Coordinate mainCell, Coordinate dependentCell);
}
