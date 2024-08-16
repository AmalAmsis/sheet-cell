package sheet.coordinate;

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
}
