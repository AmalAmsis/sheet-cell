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
}
