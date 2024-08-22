package sheet;

import sheet.coordinate.Coordinate;

public interface SheetDataModifier {

    void setCell(Coordinate coordinate, String originalValue);
    //void updateVersion();
}
