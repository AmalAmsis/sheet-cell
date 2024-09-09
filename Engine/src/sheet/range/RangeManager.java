package sheet.range;

import sheet.coordinate.Coordinate;

public interface RangeManager {
    void addRange(String name, Coordinate topLeft, Coordinate bottomRight) ;
    void removeRange(String name) ;
    void markRangeInUse(String name) ;
    void unmarkRangeInUse(String name) ;
    RangeReadActions getReadOnlyRange(String name) ;
    void displayAllRanges();

}
