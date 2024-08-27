package dto;

import sheet.cell.Cell;
import sheet.effectivevalue.EffectiveValue;

import java.util.ArrayList;
import java.util.List;

public class DTOCellImpl implements DTOCell {

    private DTOCoordinate coordinate;
    private EffectiveValue effectiveValue;
    private String originalValue;
    private int lastModifiedVersion;
    private final List<DTOCoordinate> dependsOn = new ArrayList<>();
    private final List<DTOCoordinate> influencingOn = new ArrayList<>();

    public DTOCellImpl(Cell cell) {
        //create DTOCoordinate with original cell coordinate
        this.coordinate =new DTOCoordinateImpl(cell.getCoordinate().getRow(), cell.getCoordinate().getCol());

        this.effectiveValue = cell.getEffectiveValue();
        this.originalValue = cell.getOriginalValue();
        this.lastModifiedVersion = cell.getLastModifiedVersion();
        //check if working
        //DependsOn
        for(Cell dependCell : cell.getDependsOn()) {
            DTOCoordinate dtoCoordinateWhoDependOn = new DTOCoordinateImpl(dependCell.getCoordinate().getRow(), dependCell.getCoordinate().getCol());
            this.addDToDependsOn(dtoCoordinateWhoDependOn);
        }
        //check if working
        //InfluencingOn
        for(Cell Influnecingcell : cell.getInfluencingOn()) {
            DTOCoordinate dtoCoordinateWhoInfluencingOn = new DTOCoordinateImpl(Influnecingcell.getCoordinate().getRow(), Influnecingcell.getCoordinate().getCol());
            this.addDToInfluencingOn(dtoCoordinateWhoInfluencingOn);
        }
    }

    public void setCoordinate(DTOCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    public DTOCoordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String getEffectiveValue() {
        return effectiveValue.toString();
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public int getLastModifiedVersion() {
        return lastModifiedVersion;
    }

    @Override
    public List<DTOCoordinate> getDependsOn() {
        return dependsOn;
    }

    @Override
    public List<DTOCoordinate> getInfluencingOn() {
        return influencingOn;
    }

    public void addDToDependsOn(DTOCoordinate dtoCoordinate) {
        dependsOn.add(dtoCoordinate);
    }

    public void addDToInfluencingOn(DTOCoordinate dtoCoordinate) {
        influencingOn.add(dtoCoordinate);
    }

}
