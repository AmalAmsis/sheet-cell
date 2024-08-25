package sheet.coordinate;

import dto.DTOCoordinate;
import dto.DTOCoordinateImpl;
import jaxb.schema.generated.STLCell;

import java.io.Serializable;

public class CoordinateImpl implements Coordinate , Serializable {

    int row;
    char col;

    //new ctor 22/8/24
    public CoordinateImpl(int row, char col) {
        this.row = row;
        this.col = col;
    }

    //22/8/24 - this ctor from STL object that we got from xml file,
    //we assume that we will get it to the ctor after validation test!
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

    public CoordinateImpl(char col,int row) {
        this.row = row;
        this.col = col;
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

    public int convertColToNum(){
        return (col-'A' + 1);
    }

    // Convert a string representation of a coordinate to a Coordinate object
    public static Coordinate convertStringToCoordinate(String stringCoordinate) {
        char col = stringCoordinate.charAt(0);
        int row = Integer.parseInt(stringCoordinate.substring(1));
        return new CoordinateImpl(col, row);
    }

//    public DTOCoordinate convertToDTOCoordinate(Coordinate coordinate){
//        return (new DTOCoordinateImpl(coordinate.getRow(), coordinate.getCol()));
//    }



}
