package sheet.range;

import sheet.coordinate.Coordinate;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages a collection of ranges and their associated usage counts.
 * Provides methods to add, remove, and view ranges, and to check usage counts.
 */
public class RangeManagerImpl implements RangeManager {

    private final Map<String, Range> ranges;

    public RangeManagerImpl() {
        this.ranges = new HashMap<>();
    }

    /**
     * Adds a new range to the manager.
     *
     * @param name         The unique name for the range.
     * @param topLeft      The top-left coordinate of the range.
     * @param bottomRight  The bottom-right coordinate of the range.
     * @throws Exception if the range name already exists or if the range is not contiguous.
     */
    @Override
    public void addRange(String name, Coordinate topLeft, Coordinate bottomRight) throws Exception {
        if (ranges.containsKey(name)) {
            throw new Exception("Range with this name already exists.");
        }

        if (!isContiguous(topLeft, bottomRight)) {
            throw new Exception("Range must be contiguous.");
        }

        ranges.put(name, new RangeImpl(name, topLeft, bottomRight));
    }

    /**
     * Removes an existing range from the manager.
     *
     * @param name The name of the range to be removed.
     * @throws Exception if the range is in use or not found.
     */
    @Override
    public void removeRange(String name) throws Exception {
        Range range = ranges.get(name);
        if (range != null) {
            if (range.isInUse()) {
                throw new Exception("Cannot remove range. It's currently in use by " + range.getUsageCount() + " function(s).");
            }
            ranges.remove(name);
        } else {
            throw new Exception("Range not found.");
        }
    }

    /**
     * Marks a range as being used by a function.
     *
     * @param name The name of the range.
     * @throws Exception if the range is not found.
     */
    @Override
    public void markRangeInUse(String name) throws Exception {
        Range range = ranges.get(name);
        if (range != null) {
            range.incrementUsage();
        } else {
            throw new Exception("Range not found.");
        }
    }

    /**
     * Unmarks a range as being used by a function.
     *
     * @param name The name of the range.
     * @throws Exception if the range is not found.
     */
    @Override
    public void unmarkRangeInUse(String name) throws Exception {
        Range range = ranges.get(name);
        if (range != null) {
            range.decrementUsage();
        } else {
            throw new Exception("Range not found.");
        }
    }

    /**
     * Retrieves a range by its name.
     *
     * @param name The name of the range.
     * @return The range, or null if not found.
     * @throws Exception if the range is not found.
     */
    @Override
    public RangeReadActions getReadOnlyRange(String name) throws Exception {
        RangeReadActions range = ranges.get(name);
        if (range == null) {
            throw new Exception("Range not found.");
        }
        return range;
    }

    /**
     * Displays all ranges currently managed by the manager.
     */
    @Override
    public void displayAllRanges() {
        for (Range range : ranges.values()) {
            System.out.println(range);
        }
    }

    /**
     * Checks if a range is contiguous, either in a row, column, or 2D area.
     *
     * @param topLeft      The top-left coordinate of the range.
     * @param bottomRight  The bottom-right coordinate of the range.
     * @return True if the range is contiguous, false otherwise.
     */
    private boolean isContiguous(Coordinate topLeft, Coordinate bottomRight) {
        if (topLeft.getCol() == bottomRight.getCol()) {
            return true; // Contiguous in a column
        }

        if (topLeft.getRow() == bottomRight.getRow()) {
            return true; // Contiguous in a row
        }

        return (topLeft.getRow() <= bottomRight.getRow()) && (topLeft.getCol() <= bottomRight.getCol());
    }

}
