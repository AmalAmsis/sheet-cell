package dto;

import sheetmanager.sheet.cell.Cell;
import sheetmanager.sheet.coordinate.Coordinate;
import sheetmanager.sheet.effectivevalue.EffectiveValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * DTOCellImpl is an implementation of the DTOCell interface.
 * This class is used to create a data transfer object (DTO) for a cell in a spreadsheet.
 * It serializes the cell's state for transmission between the engine and UI.
 */
public class DTOCellImpl implements DTOCell , Serializable {

    private String cellId;
    private DTOCoordinate coordinate;
    private EffectiveValue effectiveValue;
    private String originalValue;
    private int lastModifiedVersion;
    private final List<DTOCoordinate> dependsOn = new ArrayList<>();
    private final List<DTOCoordinate> influencingOn = new ArrayList<>();


    public DTOCellImpl(String cellId, DTOCoordinate coordinate, EffectiveValue effectiveValue, String originalValue, int lastModifiedVersion, List<DTOCoordinate> dependsOn, List<DTOCoordinate> influencingOn) {
        this.cellId = cellId;
        this.coordinate = coordinate;
        this.effectiveValue = effectiveValue;
        this.originalValue = originalValue;
        this.lastModifiedVersion = lastModifiedVersion;
        this.dependsOn.addAll(dependsOn);
        this.influencingOn.addAll(influencingOn);
    }


    public DTOCellImpl() {};


        /**
         * Constructs a DTOCellImpl from a Cell object.
         * Initializes the DTOCellImpl's properties based on the given Cell.
         * @param cell the Cell object to convert into a DTOCell.
         */
    public DTOCellImpl(Cell cell) {
        this.coordinate =new DTOCoordinateImpl(cell.getCoordinate().getRow(), cell.getCoordinate().getCol());
        this.effectiveValue = cell.getEffectiveValue();
        this.originalValue = cell.getOriginalValue();
        this.lastModifiedVersion = cell.getLastModifiedVersion();
        this.cellId = cell.getId();
        // Add dependencies
        for(Cell dependCell : cell.getDependsOn()) {
            DTOCoordinate dtoCoordinateWhoDependOn = new DTOCoordinateImpl(dependCell.getCoordinate().getRow(), dependCell.getCoordinate().getCol());
            this.addDToDependsOn(dtoCoordinateWhoDependOn);
        }

        // Add influences
        for(Cell Influnecingcell : cell.getInfluencingOn()) {
            DTOCoordinate dtoCoordinateWhoInfluencingOn = new DTOCoordinateImpl(Influnecingcell.getCoordinate().getRow(), Influnecingcell.getCoordinate().getCol());
            this.addDToInfluencingOn(dtoCoordinateWhoInfluencingOn);
        }
    }


    //ctor for filer sheet
    public DTOCellImpl(Cell cell, Coordinate coordinate) {

        this.cellId = cell.getId();
        int row = coordinate.getRow();
        char col = coordinate.getCol();

        this.coordinate =new DTOCoordinateImpl(row, col);
        this.effectiveValue = cell.getEffectiveValue();
        this.originalValue = cell.getOriginalValue();
        this.lastModifiedVersion = cell.getLastModifiedVersion();
        // Add dependencies
        for(Cell dependCell : cell.getDependsOn()) {
            DTOCoordinate dtoCoordinateWhoDependOn = new DTOCoordinateImpl(dependCell.getCoordinate().getRow(), dependCell.getCoordinate().getCol());
            this.addDToDependsOn(dtoCoordinateWhoDependOn);
        }

        // Add influences
        for(Cell Influnecingcell : cell.getInfluencingOn()) {
            DTOCoordinate dtoCoordinateWhoInfluencingOn = new DTOCoordinateImpl(Influnecingcell.getCoordinate().getRow(), Influnecingcell.getCoordinate().getCol());
            this.addDToInfluencingOn(dtoCoordinateWhoInfluencingOn);
        }
    }


    //todo: check if we can delete this function
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

    @Override
    public String getCellId() {
        return cellId;
    }
    /**
     * Adds a coordinate to the list of dependencies (cells this cell depends on).
     * @param dtoCoordinate the DTOCoordinate to add.
     */
    public void addDToDependsOn(DTOCoordinate dtoCoordinate) {
        dependsOn.add(dtoCoordinate);
    }
    /**
     * Adds a coordinate to the list of influences (cells that depend on this cell).
     * @param dtoCoordinate the DTOCoordinate to add.
     */
    public void addDToInfluencingOn(DTOCoordinate dtoCoordinate) {
        influencingOn.add(dtoCoordinate);
    }

    @Override
    public void setEffectiveValue(EffectiveValue effectiveValue){
        this.effectiveValue = effectiveValue;
    }

}
