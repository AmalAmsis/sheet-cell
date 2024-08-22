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

}
