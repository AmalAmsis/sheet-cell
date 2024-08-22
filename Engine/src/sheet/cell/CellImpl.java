package sheet.cell;

import dto.DTOCell;
import dto.DTOCellImpl;
import dto.DTOCoordinate;
import dto.DTOCoordinateImpl;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.EffectiveValue;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell {
    //data member
    private final String id;
    private final Coordinate coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int lastModifiedVersion;
    private final List<Cell> dependsOn = new ArrayList<>();
    private final List<Cell> influencingOn = new ArrayList<>();

    //ctor 1
    public CellImpl(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.id = coordinate.toString();
    }

    //ctor 2
    public CellImpl(Coordinate coordinate, String originalValue) {
        this(coordinate);
        this.originalValue = originalValue;
    }
    //ctor 3
    public CellImpl(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue) {
        this(coordinate, originalValue);
        this.effectiveValue = effectiveValue;
    }

    public String getId() {return id;}

    public Coordinate getCoordinate() {return coordinate;}

    @Override
    public String getOriginalValue() {
        return originalValue;
    } //AMAL

    @Override
    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    @Override
    public Object getEffectiveValue() {
        return null;
    } //AMAL

    @Override
    public int getLastModifiedVersion() {
        return lastModifiedVersion;
    }

    @Override
    public void setLastModifiedVersion(int version) {
        lastModifiedVersion = version;
    }

    @Override
    public List<Cell> getDependsOn() {
        return dependsOn;
    }

    //TO DO
    @Override
    public void addDependsOn(Cell cell) {

    }

    @Override
    public void removeDependsOn(Cell cell) { //TO DO

    }

    @Override
    public List<Cell> getInfluencingOn() {
        return influencingOn;
    }

    //TO DO
    @Override
    public void addInfluencingOn(Cell cell) {

    }

    //TO DO
    @Override
    public void removeInfluencingOn(Cell cell) {

    }

//
//    public DTOCell convertToDTOCell() {
//        DTOCoordinate dtoCoordinate = new DTOCoordinateImpl(getCoordinate().getRow(), getCoordinate().getCol());
//        DTOCell dtoCell = new DTOCellImpl();
//        dtoCell.setCoordinate(dtoCoordinate);
//        dtoCell.setEffectiveValue(effectiveValue);
//        dtoCell.setLastModifiedVersion(lastModifiedVersion);
//        dtoCell.setOriginalValue(originalValue);
//        //DependsOn
//        for(Cell cell : dependsOn) {
//            DTOCoordinate dtoCoordinateWhoDependOn = new DTOCoordinateImpl(cell.getCoordinate().getRow(), cell.getCoordinate().getCol());
//            dtoCell.addDToDependsOn(dtoCoordinateWhoDependOn);
//        }
//        //InfluencingOn
//        for(Cell cell : influencingOn) {
//            DTOCoordinate dtoCoordinateWhoInfluencingOn = new DTOCoordinateImpl(cell.getCoordinate().getRow(), cell.getCoordinate().getCol());
//            dtoCell.addDToInfluencingOn(dtoCoordinateWhoInfluencingOn);
//        }
//        return dtoCell;
//    }

}
