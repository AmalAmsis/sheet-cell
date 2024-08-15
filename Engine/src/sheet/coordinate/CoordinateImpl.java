package sheet.coordinate;

public class CoordinateImpl implements Coordinate {

    int row;
    int col;

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return col;
    }
}
