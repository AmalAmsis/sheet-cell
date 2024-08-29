package sheet.coordinate;

import jaxb.schema.generated.STLCell;
import java.io.Serializable;

/**
 * CoordinateImpl is an implementation of the Coordinate interface.
 * This class represents a cell's coordinate in a spreadsheet and supports serialization.
 */
public class CoordinateImpl implements Coordinate , Serializable {

    private int row;
    private char col;

    /** Constructs a CoordinateImpl with the specified column and row.
     * @param col the column letter of the cell.
     * @param row the row number of the cell. */
    public CoordinateImpl(char col,int row) {
        this.row = row;
        this.col = col;
    }

    /** Constructs a CoordinateImpl from an STLCell object.
     * @param stlCell the STLCell object from an XML file. */
    public CoordinateImpl(STLCell stlCell) {
        this.row = stlCell.getRow();
        this.col = stlCell.getColumn().charAt(0);
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public char getCol() {return col;}

    @Override
    public String toString() {
        return col + ":" + row;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            if (((Coordinate) obj).getCol() == col && ((Coordinate) obj).getRow() == row) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
         //row*10 + col.
        return (convertColToNum()+(row*10));
    }

    /** Converts the column character to its numerical equivalent.
     * @return an integer representing the column's position in the alphabet. */
    public int convertColToNum(){
        return (col-'A' + 1);
    }


    //todo : delete
    /**
     * Converts a string representation of a coordinate to a Coordinate object.
     * The string should be in the format where the first character represents the column
     * (e.g., 'A', 'B', etc.) and the following characters represent the row number (e.g., "1", "2", etc.).
     * The column letter is converted to uppercase to handle both lowercase and uppercase input.
     *
     * @param stringCoordinate the string representation of the coordinate (e.g., "A1", "b2").
     * @return a Coordinate object corresponding to the provided string.*/
    public static Coordinate convertStringToCoordinate(String stringCoordinate) {
        stringCoordinate = stringCoordinate.toUpperCase();
        char col = stringCoordinate.charAt(0);
        int row = Integer.parseInt(stringCoordinate.substring(1));
        return new CoordinateImpl(col, row);
    }
}
