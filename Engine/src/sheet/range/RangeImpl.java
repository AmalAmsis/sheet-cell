package sheet.range;

import sheet.coordinate.Coordinate;

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
