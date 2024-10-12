package dto;

import sheetmanager.sheet.coordinate.Coordinate;
import sheetmanager.sheet.range.RangeReadActions;

import java.util.ArrayList;
import java.util.List;

public class DTORangeImpl implements DTORange {

    private String name;
    private DTOCoordinate topLeft;
    private DTOCoordinate bottomRight;
    private int usageCount;
    private List<DTOCoordinate> coordinates;


    public DTORangeImpl(RangeReadActions range) {
        this.name = range.getName();
        this.topLeft = new DTOCoordinateImpl(range.getTopLeft());
        this.bottomRight = new DTOCoordinateImpl(range.getBottomRight());
        this.usageCount = range.getUsageCount();
        this.coordinates = createDTOCoordinatesList(range);
    }

    private List<DTOCoordinate> createDTOCoordinatesList(RangeReadActions range) {
        List<DTOCoordinate> dtoCoordinates = new ArrayList<>();
        List<Coordinate> coordinates = range.getCoordinates();
        for (Coordinate coordinate : coordinates) {
            dtoCoordinates.add(new DTOCoordinateImpl(coordinate));
        }
        return dtoCoordinates;
    }


    @Override
    public DTOCoordinate getTopLeftCoordinate() {
        return topLeft;
    }

    @Override
    public DTOCoordinate getBottomRightCoordinate() {
        return bottomRight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getUsageCount() {
        return usageCount;
    }

    @Override
    public List<DTOCoordinate> getCoordinates() {
        return coordinates;
    }




}
