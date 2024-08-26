package dto;

import sheet.effectivevalue.EffectiveValue;

import java.util.List;

public interface DTOCell {

    //getters
    DTOCoordinate getCoordinate();
    EffectiveValue getEffectiveValue();
    String getOriginalValue();
    int getLastModifiedVersion();
    List<DTOCoordinate> getDependsOn();
    List<DTOCoordinate> getInfluencingOn();

}
