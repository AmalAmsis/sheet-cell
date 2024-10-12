package dto;

import sheetmanager.sheet.coordinate.Coordinate;

import java.io.Serializable;

/**
 * DTOCoordinateImpl is an implementation of the DTOCoordinate interface.
 * This class is used to represent a cell's coordinate in a spreadsheet,
 * with row and column information, and can be serialized.
 */
public class DTOCoordinateImpl implements DTOCoordinate, Serializable {
    private int row;
    private char col;

    /**
     * Constructs a DTOCoordinateImpl with the specified row and column.
     * @param row the row number of the cell.
     * @param col the column letter of the cell.
     */
    public DTOCoordinateImpl(int row, char col) {
        this.row = row;
        this.col = col;
    }

    //****************************************************************************************************//

    public DTOCoordinateImpl(Coordinate coordinate) {
        this.row = coordinate.getRow();
        this.col = coordinate.getCol();
    }



        //****************************************************************************************************//

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public char getCol() {return col;}

    /**
     * Returns a string representation of the coordinate in the format "col:row".
     * @return a String representing the cell's coordinate.
     */
    @Override
    public String toString() {
        return col + ":" + row;
    }

}
