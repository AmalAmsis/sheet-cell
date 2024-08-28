package sheet.cell;

import dto.DTOCell;
import jaxb.schema.generated.STLCell;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;

import java.util.List;
import java.util.Set;

public interface Cell {
    String getOriginalValue();
    EffectiveValue getEffectiveValue();
    int getLastModifiedVersion();
    void setLastModifiedVersion(int version);
    List<Cell> getDependsOn();
    void addToDependsOn(Cell cell);
    void removeDependsOn(Cell cell);
    List<Cell> getInfluencingOn();
    void addToInfluencingOn(Cell cell);
    void removeInfluencingOn(Cell cell);
    STLCell convertFromCellToSTLCell();
    Coordinate getCoordinate();
    EffectiveValue calculateEffectiveValue(String originalValue);
    void setEffectiveValue(EffectiveValue effectiveValue);
    String getId();
    int updateValue(String originalValue);
    void updateValueHelper(String originalValue, Set<Cell> visitedCells);
    public void removeAllDependsOn();
     List<Cell> detectCycle();
    List<Cell> detectCycleHelper(Set<Cell> visited, Set<Cell> recStack, List<Cell> path);

}
