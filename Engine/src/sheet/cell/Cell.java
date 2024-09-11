package sheet.cell;

import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;
import java.util.List;
import java.util.Set;

/**
 * Cell represents the interface for a cell in a spreadsheet.
 * This interface provides methods to manage a cell's value, dependencies, and interactions with other cells.
 */
public interface Cell {

    /** Returns the original value of the cell before any calculations.
     * @return a String representing the original value. */
    String getOriginalValue();

    /** Returns the effective value of the cell after all calculations.
     * @return an EffectiveValue representing the calculated value. */
    EffectiveValue getEffectiveValue();

    /** Returns the version number of the last modification made to the cell.
     * @return an integer representing the last modified version. */
    int getLastModifiedVersion();

    /** Sets the version number of the last modification made to the cell.
     * @param version the version number to set. */
    void setLastModifiedVersion(int version);

    /** Returns a list of cells that this cell depends on.
     * @return a List of Cell objects that this cell depends on. */
    List<Cell> getDependsOn();

    String getdependsOnRangeName();

    void setdependsOnRangeName(String rangeName);

    /** Adds a cell to the list of cells that this cell depends on.
     * @param cell the Cell to add to the depends on list. */
    void addToDependsOn(Cell cell);

    //todo : why we dont use this one?
    /** Removes a cell from the list of cells that this cell depends on.
     * @param cell the Cell to remove from the depends on list. */
    void removeDependsOn(Cell cell);

    /** Returns a list of cells that depend on this cell.
     * @return a List of Cell objects that depend on this cell. */
    List<Cell> getInfluencingOn();

    /** Adds a cell to the list of cells that depend on this cell.
     * @param cell the Cell to add to the influencing on list. */
    void addToInfluencingOn(Cell cell);

    /** Removes a cell from the list of cells that depend on this cell.
     * @param cell the Cell to remove from the influencing on list. */
    void removeInfluencingOn(Cell cell);

    //todo: I dont think we need this function.
    /** Converts this cell into an STLCell object for serialization.
     * @return an STLCell object representing this cell. */
    STLCell convertFromCellToSTLCell();

    /** Returns the coordinate of the cell.
     * @return a Coordinate object representing the cell's position. */
    Coordinate getCoordinate();

    /** Calculates the effective value of the cell based on its original value.
     * @param originalValue the original value to calculate from.
     * @return an EffectiveValue representing the calculated value. */
    EffectiveValue calculateEffectiveValue(String originalValue);

    /** Sets the effective value of the cell.
     * @param effectiveValue the EffectiveValue to set. */
    void setEffectiveValue(EffectiveValue effectiveValue);

    /** Returns the unique identifier of the cell.
     * @return a String representing the cell's ID. */
    String getId();

    /** Updates the value of the cell and returns the new version number.
     * @param originalValue the new value to set.
     * @return an integer representing the new version number. */
    int updateValue(String originalValue);

    /** Helper method to update the cell's value recursively, avoiding cycles.
     * @param originalValue the new value to set.
     * @param visitedCells a set of visited cells to track recursion. */
    void updateValueHelper(String originalValue, Set<Cell> visitedCells);

    /** Removes all cells from the depends on list. */
    void removeAllDependsOn();

    /** Detects a cycle in the cell's dependencies.
     * @return a List of Cell objects that form a cycle. */
     List<Cell> detectCycle();

    /** Helper method to detect cycles recursively.
     * @param visited a set of visited cells.
     * @param recStack a set representing the recursion stack.
     * @param path a list to track the path of the cycle.
     * @return a List of Cell objects that form a cycle. */
    List<Cell> detectCycleHelper(Set<Cell> visited, Set<Cell> recStack, List<Cell> path);



}
