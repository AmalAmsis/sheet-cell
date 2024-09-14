package dto;

import sheet.range.RangeReadActions;

import java.util.List;

public interface DTORange {
    DTOCoordinate getTopLeftCoordinate();
    DTOCoordinate getBottomRightCoordinate();
    String getName();
    int getUsageCount();
    List<DTOCoordinate> getCoordinates();

}
