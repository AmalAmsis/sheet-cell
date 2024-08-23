package sheet.coordinate;

import dto.DTOCoordinate;
import dto.DTOCoordinateImpl;

public class CoordinateImpl implements Coordinate {

    int row;
    char col;

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
        // row*10 + col.
        return (convertColToNum()+(row*10));
    }

    public int convertColToNum(){
        return (col-'A' + 1);
    }

    //?????
    // Convert a string representation of a coordinate to a Coordinate object
    public static Coordinate convertStringToCoordinate(String stringCoordinate) {
        char col = stringCoordinate.charAt(0);
        int row = Integer.parseInt(stringCoordinate.substring(1));
        return new CoordinateImpl(col, row);
    }

    public DTOCoordinate convertToDTOCoordinate(Coordinate coordinate){
        return (new DTOCoordinateImpl(coordinate.getRow(), coordinate.getCol()));
    }
}
