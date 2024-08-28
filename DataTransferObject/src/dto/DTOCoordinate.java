package dto;

/**
 * DTOCoordinate represents the data transfer object for a cell's coordinate in a spreadsheet.
 * This interface provides methods to retrieve the row and column of a cell.
 */
public interface DTOCoordinate {
    /**
     * Returns the row number of the cell.
     * @return an integer representing the row number.
     */
    int getRow();
    /**
     * Returns the column letter of the cell.
     * @return a char representing the column letter.
     */
    char getCol();
}
