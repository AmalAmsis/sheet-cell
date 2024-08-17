package dto;

import java.util.List;

public interface DTOCell {

    //getters
    DTOCoordinate getCoordinate();
    Object getEffectiveValue();
    String getOriginalValue();
    int getLastModifiedVersion();
    List<DTOCoordinate> getDependsOn();
    List<DTOCoordinate> getInfluencingOn();

    //setter
    void setCoordinate(DTOCoordinate coordinate);
    void setEffectiveValue(Object o);
    void setOriginalValue(String o);
    void setLastModifiedVersion(int version);
    void addDToDependsOn(DTOCoordinate dtoCoordinate);
    void addDToInfluencingOn(DTOCoordinate dtoCoordinate);

}
