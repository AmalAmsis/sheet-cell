package sheet.cell.imp;

import sheet.api.EffectiveValue;
import sheet.cell.api.Cell;

import java.util.List;

public class CellImpl implements Cell {
    //data member

    private String originalValue;
    private EffectiveValue effectiveValue;
    private int lastModifiedVersion;
    private final List<Cell> dependsOn;
    private final List<Cell> influencingOn;


    @Override
    public String getOriginalValue() {
        return "";
    }

    @Override
    public void setOriginalValue(String originalValue) {

    }

    @Override
    public Object getEffectiveValue() {
        return null;
    }

    @Override
    public int getLastModifiedVersion() {
        return 0;
    }

    @Override
    public void setLastModifiedVersion(int version) {

    }

    @Override
    public List<Cell> getDependsOn() {
        return List.of();
    }

    @Override
    public void addDependsOn(Cell cell) {

    }

    @Override
    public void removeDependsOn(Cell cell) {

    }

    @Override
    public List<Cell> getInfluencingOn() {
        return List.of();
    }

    @Override
    public void addInfluencingOn(Cell cell) {

    }

    @Override
    public void removeInfluencingOn(Cell cell) {

    }
}
