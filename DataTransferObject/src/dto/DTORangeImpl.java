package dto;

import sheet.range.Range;

public class DTORangeImpl implements DTORange {

    private String name;
    private DTOCoordinate topLeft;
    private DTOCoordinate bottomRight;
    private int usageCount;


    public DTORangeImpl(Range range) {
        this.name = range.getName();
        this.topLeft = new DTOCoordinateImpl(range.getTopLeft());
        this.bottomRight = new DTOCoordinateImpl(range.getBottomRight());
        this.usageCount = range.getUsageCount();
    }

    @Override
    public DTOCoordinate getTopLeftCoordinate() {
        return topLeft;
    }

    @Override
    public DTOCoordinate getBottomRightCoordinate() {
        return bottomRight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getUsageCount() {
        return usageCount;
    }





}
