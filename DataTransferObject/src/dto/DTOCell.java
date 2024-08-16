package dto;

import java.util.List;

public interface DTOCell {

    //getters
    DTOCoordinate getCoordinate();
    Object getEffectiveValue();
    String getOriginalValue();
    int getLastModifiedVersion();
    List<DTOCell> getDependsOn();
    List<DTOCell> getInfluencingOn();

    //setter
    void setCoordinate(DTOCoordinate coordinate);
    void setEffectiveValue(Object o);
    void setOriginalValue(String o);
    void setLastModifiedVersion(int version);
    void addDTOCellToDependsOn(DTOCell dtoCell);
    void addDTOCellToInfluencingOn(DTOCell dtoCell);

}
