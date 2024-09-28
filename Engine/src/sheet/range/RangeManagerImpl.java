package sheet.range;

import sheet.coordinate.Coordinate;

import java.util.HashMap;
import java.util.Map;


// צריך לתקן את האקספשנים
/**
 * Manages a collection of ranges and their associated usage counts.
 * Provides methods to add, remove, and view ranges, and to check usage counts.
 */
public class RangeManagerImpl implements RangeManager {

    private final Map<String, Range> ranges;

    public RangeManagerImpl() {
        this.ranges = new HashMap<>();
    }

    //********************************************************************************************************//


    public Map<String, Range> getRanges() {
        return this.ranges;
    }

    @Override
    public RangeManager createDeepCopy() {
        RangeManagerImpl deepCopyManager = new RangeManagerImpl();

        // ביצוע העתקה עמוקה של כל טווח במפה
        for (Map.Entry<String, Range> entry : this.ranges.entrySet()) {
            // הנחנו שלמחלקה Range יש מתודה createDeepCopy שמבצעת העתקה עמוקה לטווח
            Range copiedRange = entry.getValue().createDeepCopy();
            deepCopyManager.ranges.put(entry.getKey(), copiedRange);
        }

        return deepCopyManager;
    }


    //********************************************************************************************************//

    @Override
    public Boolean isRangeExist(String range) {
        return this.ranges.containsKey(range);
    }


    /**
     * Adds a new range to the manager.
     *
     * @param name         The unique name for the range.
     * @param topLeft      The top-left coordinate of the range.
     * @param bottomRight  The bottom-right coordinate of the range.
     */
    @Override
    public void addRange(String name, Coordinate topLeft, Coordinate bottomRight)  {
        if (ranges.containsKey(name)) {
            throw new IllegalArgumentException("Range with the name '" + name + "' already exists with coordinates topLeft: "
                    + ranges.get(name).getTopLeftCoordinate()
                    + " bottomRight: "
                    + ranges.get(name).getBottomRightCoordinate()
                    + ". Please choose a different name or remove the existing range.");
        }

        if (!isContiguous(topLeft, bottomRight)) {
            throw new IllegalArgumentException("Range must be contiguous. range: " + name + " topLeft: " + topLeft + " bottomRight: " + bottomRight);
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
    public void removeRange(String name)  {
        Range range = ranges.get(name);
        if (range != null) {
            if (range.isInUse()) {
                throw new IllegalArgumentException("Cannot remove range. It's currently in use by " + range.getUsageCount() + " function(s).");
            }
            ranges.remove(name);
        } else {
            throw new IllegalArgumentException("Range not found.");
        }
    }

    /**
     * Marks a range as being used by a function.
     *
     * @param name The name of the range.
     * @throws Exception if the range is not found.
     */
    @Override
    public void markRangeInUse(String name)  {
        Range range = ranges.get(name);
        if (range != null) {
            range.incrementUsage();
        } else {
            throw new IllegalArgumentException("Range not found.");
        }
    }

    /**
     * Unmarks a range as being used by a function.
     *
     * @param name The name of the range.
     * @throws Exception if the range is not found.
     */
    @Override
    public void unmarkRangeInUse(String name)  {
        Range range = ranges.get(name);
        if (range != null) {
            range.decrementUsage();
        } else {
            throw new IllegalArgumentException("Range not found.");
        }
    }

    /**
     * Retrieves a range by its name.
     *
     * @param name The name of the range.
     * @return The range as RangeReadActions, or null if not found.
     */
    @Override
    public RangeReadActions getReadOnlyRange(String name)  {
        RangeReadActions range = ranges.get(name);
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
