package sheetmanager.sheet.range;

import sheetmanager.sheet.coordinate.Coordinate;

import java.util.List;

public interface RangeReadActions {
    String getName();
    Coordinate getTopLeft();
    Coordinate getBottomRight();
    int getUsageCount();
    void incrementUsage();
    void decrementUsage();
    boolean isInUse();
    List<Coordinate> getCoordinates();
    @Override
    String toString();

    String getTopLeftCoordinate();

    String getBottomRightCoordinate();

    Range createDeepCopy();
}
