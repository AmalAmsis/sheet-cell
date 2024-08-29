package sheet;

import sheet.cell.Cell;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;
import java.util.Map;


/**
 * SheetDataRetriever provides methods for retrieving data from a spreadsheet.
 * This interface includes functions to access the sheet's metadata, cell values, dimensions,
 * and the underlying data structure.
 */
public interface SheetDataRetriever {

    /** Returns the current version of the sheet.
     * @return an integer representing the version number of the sheet. */
    int getVersion();

    /** Returns the title of the sheet.
     * @return a String representing the title of the sheet. */
    String getTitle();

    /** Retrieves the effective value of the cell at the specified coordinate.
     * @param coordinate the coordinate of the cell to retrieve.
     * @return an EffectiveValue object representing the calculated value of the cell. */
    EffectiveValue getCellEffectiveValue(Coordinate coordinate);

    /** Checks if a cell exists at the specified coordinate in the sheet.
     * @param coordinate the coordinate to check.
     * @return true if the cell exists, false otherwise. */
    boolean isCellInSheet(Coordinate coordinate);

    /** Returns the height of the rows in the sheet.
     * @return an integer representing the height of the rows. */
    int getHeightOfRows();

    /** Returns the width of the columns in the sheet.
     * @return an integer representing the width of the columns. */
    int getWidthOfCols();

    /** Returns the number of rows in the sheet.
     * @return an integer representing the number of rows. */
    int getNumOfRows();

    /** Returns the number of columns in the sheet.
     * @return an integer representing the number of columns. */
    int getNumOfCols();
    Coordinate convertStringToCoordinate(String stringCoordinate);

    /** Returns the board of cells in the sheet.
     * The map's key is a string representation of the cell's coordinate, and the value is the Cell object.
     * @return a Map containing the cells of the sheet. */
    Map<String, Cell> getBoard();


    /** Adds a dependent cell to track dependencies between cells in the sheet.
     * @param mainCell the coordinate of the main cell.
     * @param dependentCell the coordinate of the cell that depends on the main cell. */
    void addDependentCell(Coordinate mainCell, Coordinate dependentCell);
}
