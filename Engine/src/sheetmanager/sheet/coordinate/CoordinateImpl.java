package sheetmanager.sheet.coordinate;

import uploadfilemanager.jaxb.generated.STLCell;

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
    public void setCol(char col) {
        this.col = col;
    }
    @Override
    public void setRow(int row) {
        this.row = row;
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

}
