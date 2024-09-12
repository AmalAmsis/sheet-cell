package sheet.range;

import jaxb.schema.generated.STLRange;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;

import java.util.ArrayList;
import java.util.List;

public class RangeImpl implements Range {
    private final String name;
    private final Coordinate topLeft;
    private final Coordinate bottomRight;
    private int usageCount;

    public RangeImpl(String name, Coordinate topLeft, Coordinate bottomRight) {
        this.name = name;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.usageCount = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Coordinate getTopLeft() {
        return topLeft;
    }

    @Override
    public Coordinate getBottomRight() {
        return bottomRight;
    }

    @Override
    public int getUsageCount() {
        return usageCount;
    }

    @Override
    public void incrementUsage() {
        this.usageCount++;
    }

    @Override
    public void decrementUsage() {
        if (this.usageCount > 0) {
            this.usageCount--;
        }
    }

    @Override
    public boolean isInUse() {
        return usageCount > 0;
    }

    /**
     * Returns all coordinates within the range.
     * @return List of coordinates in the range
     */
    @Override
    public List<Coordinate> getCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int row = topLeft.getRow(); row <= bottomRight.getRow(); row++) {
            for (char col = topLeft.getCol(); col <= bottomRight.getCol(); col++) {
                coordinates.add(new CoordinateImpl(col, row));
            }
        }
        return coordinates;
    }

    @Override
    public String toString() {
        return "Range{" +
                "name='" + name + '\'' +
                ", topLeft=" + topLeft +
                ", bottomRight=" + bottomRight +
                ", usageCount=" + usageCount +
                '}';
    }

}
