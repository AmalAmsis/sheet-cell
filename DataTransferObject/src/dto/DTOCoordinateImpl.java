package dto;

public class DTOCoordinateImpl implements DTOCoordinate {
    int row;
    char col;

    //ctor
    public DTOCoordinateImpl(int row, char col) {
        this.row = row;
        this.col = col;
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

}
