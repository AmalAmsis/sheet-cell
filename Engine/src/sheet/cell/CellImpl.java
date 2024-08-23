package sheet.cell;

import dto.DTOCell;
import expression.ExpressionEvaluator;
import sheet.SheetDataRetriever;
import dto.DTOCellImpl;
import dto.DTOCoordinate;
import dto.DTOCoordinateImpl;
import jaxb.schema.generated.STLCell;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;
import sheet.effectivevalue.EffectiveValue;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell {
    //data member
    private final String id; // do we need it ?
    private final Coordinate coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int lastModifiedVersion;
    private final List<Cell> dependsOn;
    private final List<Cell> influencingOn;
    private SheetDataRetriever sheet;

    /*
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
     */
    //22/8/24 - this ctor from STL object that we got from xml file,
    //we assume that we will get it to the ctor after validation test!
    public CellImpl(STLCell stlCell) {
        this.originalValue = stlCell.toString();
        Coordinate myCoordinate = new CoordinateImpl(stlCell);
        this.coordinate = myCoordinate;
        this.id = myCoordinate.toString();
        this.lastModifiedVersion =1;

        //TO DO --> DEPENDSON AND INFLUENING ON.
    }
    // 22/8/24
    public STLCell convertFromCellToSTLCell() {
        //preparation for create an STLCell
        String myOriginalValue = this.getOriginalValue();
        int myRow = this.getCoordinate().getRow();
        char myCol = this.getCoordinate().getCol();

        //create new STLCell
        STLCell stlCell = new STLCell();
        stlCell.setSTLOriginalValue(myOriginalValue);
        stlCell.setRow(myRow);
        stlCell.setColumn(Character.toString(myCol));

        return stlCell;
    }


    public CellImpl(Coordinate coordinate, int lastModifiedVersion, SheetDataRetriever sheet)
    {
        this.effectiveValue = null;
        this.originalValue = "";
        this.coordinate = coordinate;
        this.lastModifiedVersion = lastModifiedVersion;
        this.dependsOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();
        this.sheet = sheet;

    }

    //public String getId() {return id;}

    public Coordinate getCoordinate() {return coordinate;}

    @Override
    public EffectiveValue calculateEffectiveValue(String originalValue) {
        ExpressionEvaluator.evaluate(originalValue, sheet, this.coordinate);
        return effectiveValue;
    }

    @Override
    public DTOCell convertToDTOCell() {
        return null;
    }

    public void updateValue(String originalValue)
    {
        EffectiveValue  previousEffectiveValue = this.effectiveValue;
        this.effectiveValue = calculateEffectiveValue(originalValue);
        try {
            for (Cell cell : influencingOn) {
                cell.setEffectiveValue(calculateEffectiveValue(cell.getOriginalValue()));
            }
            this.originalValue = originalValue;
        }
        catch (Exception e)
        {
            this.effectiveValue = previousEffectiveValue;
            for (Cell cell : influencingOn) {
                cell.setEffectiveValue(calculateEffectiveValue(cell.getOriginalValue()));
            }
            throw e;
        }
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    } //AMAL


    private void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public void setEffectiveValue(EffectiveValue effectiveValue) {
        this.effectiveValue = effectiveValue;
    }

    @Override
    public EffectiveValue getEffectiveValue() {
        return effectiveValue;
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
}