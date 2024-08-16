package sheet.cell;

import sheet.api.EffectiveValue;

import java.util.List;

public class CellImpl implements Cell {
    //data member
    private final String id;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int lastModifiedVersion;
    private List<Cell> dependsOn;
    private List<Cell> influencingOn;

    public CellImpl(String id) {
        this.id = id;
    }

    @Override
    public String getOriginalValue() {
        return "";
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
        return List.of();
    }

    @Override
    public void addDependsOn(Cell cell) { //TO DO

    }

    @Override
    public void removeDependsOn(Cell cell) { //TO DO

    }

    @Override
    public List<Cell> getInfluencingOn() {
        return List.of();
    }

    @Override
    public void addInfluencingOn(Cell cell) { //TO DO

    }

    @Override
    public void removeInfluencingOn(Cell cell) { //TO DO

    }
}
