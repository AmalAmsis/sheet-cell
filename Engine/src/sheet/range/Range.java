package sheet.range;

import sheet.coordinate.Coordinate;

public interface Range {
    String getName();
    Coordinate getTopLeft();
    Coordinate getBottomRight();
    int getUsageCount();
    void incrementUsage();
    void decrementUsage();
    boolean isInUse();
    @Override
    String toString();
}
