package sheetmanager.sheet.range;

import sheetmanager.sheet.coordinate.Coordinate;

import java.util.Map;

public interface RangeManager {
    void addRange(String name, Coordinate topLeft, Coordinate bottomRight) ;
    void removeRange(String name) ;
    void markRangeInUse(String name) ;
    void unmarkRangeInUse(String name) ;
    RangeReadActions getReadOnlyRange(String name) ;
    void displayAllRanges();
    Boolean isRangeExist(String range);

    //*********************************************************YARDEN
    Map<String, Range> getRanges();

    RangeManager createDeepCopy();
}
