package sheet;

import sheet.coordinate.Coordinate;

/**
 * SheetDataModifier provides methods for modifying the data in a spreadsheet.
 * This interface allows setting and removing cells within the sheet.
 */
public interface SheetDataModifier {

    /** Sets the value of the cell at the specified coordinate.
     * If the cell does not exist, it will be created.
     * @param coordinate the coordinate of the cell to set.
     * @param originalValue the value to set in the cell.
     * @return an integer representing the version number after the cell is updated. */
    int setCell(Coordinate coordinate, String originalValue);

    /** Removes the cell at the specified coordinate.
     * If the cell does not exist, this method does nothing.
     * @param coordinate the coordinate of the cell to remove. */
    void removeCell(Coordinate coordinate);

    void addRangeToManager(String rangeName, String range) throws Exception;
    void removeRangeFromManager(String rangeName) throws Exception;
}