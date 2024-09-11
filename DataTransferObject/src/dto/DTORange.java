package dto;

public interface DTORange {
    DTOCoordinate getTopLeftCoordinate();
    DTOCoordinate getBottomRightCoordinate();
    String getName();
    int getUsageCount();

}
