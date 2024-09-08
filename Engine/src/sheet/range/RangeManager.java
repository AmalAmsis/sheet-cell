package sheet.range;

import sheet.coordinate.Coordinate;

public interface RangeManager {
    void addRange(String name, Coordinate topLeft, Coordinate bottomRight) throws Exception;
    void removeRange(String name) throws Exception;
    void markRangeInUse(String name) throws Exception;
    void unmarkRangeInUse(String name) throws Exception;
    RangeReadActions getReadOnlyRange(String name) ;
    void displayAllRanges();

}
