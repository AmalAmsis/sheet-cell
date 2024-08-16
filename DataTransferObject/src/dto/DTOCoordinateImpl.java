package dto;

public class DTOCoordinateImpl implements DTOCoordinate {
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
