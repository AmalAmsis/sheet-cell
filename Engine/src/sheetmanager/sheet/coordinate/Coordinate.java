package sheetmanager.sheet.coordinate;

/**
 * Coordinate represents the interface for a cell's coordinate in a spreadsheet.
 * This interface provides methods to retrieve the row and column of a cell.
 */
public interface Coordinate {

    /**
     * Returns the row number of the cell.
     * @return an integer representing the row number.*/
    int getRow();

    /**
     * Returns the column letter of the cell.
     * @return a char representing the column letter.*/
    char getCol();

     void setCol(char col);
     void setRow(int row);


}

