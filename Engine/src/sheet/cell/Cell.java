package sheet.cell;

import dto.DTOCell;
import sheet.coordinate.Coordinate;

import java.util.List;

public interface Cell {
    String getOriginalValue();
    void setOriginalValue(String originalValue);
    Object getEffectiveValue();
    int getLastModifiedVersion();
    void setLastModifiedVersion(int version);
    List<Cell> getDependsOn();
    void addDependsOn(Cell cell);
    void removeDependsOn(Cell cell);
    List<Cell> getInfluencingOn();
    void addInfluencingOn(Cell cell);
    void removeInfluencingOn(Cell cell);
    public Coordinate getCoordinate();
}
