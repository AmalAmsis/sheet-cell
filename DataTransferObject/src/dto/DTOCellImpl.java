package dto;

import java.util.ArrayList;
import java.util.List;

public class DTOCellImpl implements DTOCell {

    private DTOCoordinate coordinate;
    private Object effectiveValue;
    private String originalValue;
    private int lastModifiedVersion;
    private  List<DTOCell> dependsOn = new ArrayList<>();
    private  List<DTOCell> influencingOn = new ArrayList<>();

    public void setCoordinate(DTOCoordinate coordinate) {
        this.coordinate = coordinate;
    }
    public DTOCoordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public Object getEffectiveValue() {
        return effectiveValue;
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
    public List<DTOCell> getDependsOn() {
        return dependsOn;
    }

    @Override
    public List<DTOCell> getInfluencingOn() {
        return influencingOn;
    }

    @Override
    public void setEffectiveValue(Object o) {
        this.effectiveValue = o;
    }

    @Override
    public void setOriginalValue(String o) {
        this.originalValue = o;
    }

    @Override
    public void setLastModifiedVersion(int version) {
        this.lastModifiedVersion = version;
    }

    @Override
    public void addDTOCellToDependsOn(DTOCell dtoCell) {
        dependsOn.add(dtoCell);
    }

    @Override
    public void addDTOCellToInfluencingOn(DTOCell dtoCell) {
        influencingOn.add(dtoCell);
    }

}
