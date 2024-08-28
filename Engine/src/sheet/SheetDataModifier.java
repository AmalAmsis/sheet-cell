package sheet;

import sheet.coordinate.Coordinate;

public interface SheetDataModifier {

    int setCell(Coordinate coordinate, String originalValue);
    void removeCell(Coordinate coordinate);
    //void updateVersion();
}