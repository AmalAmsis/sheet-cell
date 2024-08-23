package sheet.cell;

import dto.DTOCell;
import jaxb.schema.generated.STLCell;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;

import java.util.List;

public interface Cell {
    String getOriginalValue();
    EffectiveValue getEffectiveValue();
    int getLastModifiedVersion();
    void setLastModifiedVersion(int version);
    List<Cell> getDependsOn();
    void addDependsOn(Cell cell);
    void removeDependsOn(Cell cell);
    List<Cell> getInfluencingOn();
    void addInfluencingOn(Cell cell);
    void removeInfluencingOn(Cell cell);
    STLCell convertFromCellToSTLCell();
    Coordinate getCoordinate();
    EffectiveValue calculateEffectiveValue(String originalValue);
    DTOCell convertToDTOCell();
    void setEffectiveValue(EffectiveValue effectiveValue);

}
