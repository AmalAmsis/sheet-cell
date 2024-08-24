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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CellImpl implements Cell, Serializable {
    //data member
    private final String id; // do we need it ?
    private final Coordinate coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int lastModifiedVersion;
    private final List<Cell> dependsOn;
    private final List<Cell> influencingOn;
    private SheetDataRetriever sheet;

    public CellImpl(Coordinate coordinate, int lastModifiedVersion, SheetDataRetriever sheet) {
        this.effectiveValue = null;
        this.originalValue = "";
        this.coordinate = coordinate;
        this.lastModifiedVersion = lastModifiedVersion;
        this.dependsOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();
        this.sheet = sheet;
        this.id = coordinate.toString();

    }


    //22/8/24 - this ctor from STL object that we got from xml file,
    //we assume that we will get it to the ctor after validation test!
    public CellImpl(STLCell stlCell) {
        this.originalValue = stlCell.toString();
        Coordinate myCoordinate = new CoordinateImpl(stlCell);
        this.coordinate = myCoordinate;
        this.id = myCoordinate.toString();
        this.lastModifiedVersion = 1;

        this.dependsOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();
        //TO DO --> DEPENDSON AND INFLUENING ON.
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public EffectiveValue calculateEffectiveValue(String originalValue) {
        ExpressionEvaluator.evaluate(originalValue, sheet, this.coordinate);
        return effectiveValue;
    }


    //לא לשכוח להוסיף בדיקה אם התוצאה מתאימה לגודל תא בגליון
    @Override
    public void updateValue(String originalValue) {
        //EffectiveValue previousEffectiveValue = this.effectiveValue;
        try {
            updateValueHelper(originalValue);
            this.originalValue = originalValue;
        } catch (Exception e) {
            //this.effectiveValue = previousEffectiveValue;
            updateValueHelper(originalValue);
            throw e;
        }
    }

    @Override
    public void updateValueHelper(String originalValue) {
        this.effectiveValue = calculateEffectiveValue(originalValue);
        for (Cell cell : influencingOn) {
            //cell.setEffectiveValue(calculateEffectiveValue(cell.getOriginalValue()));
            cell.updateValueHelper(cell.getOriginalValue());
        }
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }


    private void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public void setEffectiveValue(EffectiveValue effectiveValue) {
        this.effectiveValue = effectiveValue;
    }

    @Override
    public EffectiveValue getEffectiveValue() {
        return effectiveValue;
    }

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
    public void addToDependsOn(Cell cell) {
        try {
            cell.addToInfluencingOn(this);
            dependsOn.add(cell);
        }
        catch (Exception e) {
            throw e;
        }

    }


    @Override
    public void removeAllDependsOn() {
        for (Cell cell : dependsOn) {
            this.removeDependsOn(cell);
        }
    }

    //TO DO
    @Override
    public void removeDependsOn(Cell cell) {
        this.dependsOn.remove(cell);
        cell.removeInfluencingOn(this);
    }

    @Override
    public List<Cell> getInfluencingOn() {
        return influencingOn;
    }

    //TO DO
    @Override
    public void addToInfluencingOn(Cell cell) {
        this.influencingOn.add(cell);
        if (isCircle())
        {
            this.influencingOn.remove(cell);
            String message = String.format("Circle");//TO DO: add a clear message
            throw new  IllegalArgumentException(message);
        }

    }

    //TO DO
    @Override
    public void removeInfluencingOn(Cell cell) {
        influencingOn.remove(cell);
        //cell.removeDependsOn(cell);??????????????????????????????????????????

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isCircle() {
        return isCircleHelper(new HashSet<>(), new HashSet<>());
    }

    /**
     * Helper method to detect cycles using DFS.
     * If a cell influences another cell, there is a directed edge (or arc) from the influencing cell to the influenced cell.
     *
     * @param visited Keeps track of fully processed cells (Black Nodes).
     * @param recStack Tracks the current path in DFS (Gray Nodes).
     * @return true if a cycle (back edge) is found, false otherwise.
     */
    public boolean isCircleHelper(Set<Cell> visited, Set<Cell> recStack) {
        // If this cell is already in the recursion stack, a back edge (cycle) is detected.
        if (recStack.contains(this)) {
            return true;
        }

        // If this cell is already visited, it's fully processed, so no cycle here.
        if (visited.contains(this)) {
            return false;
        }

        // Mark this cell as visited and add it to the recursion stack.
        visited.add(this);
        recStack.add(this);

        // Recursively check all influenced cells.
        for (Cell cell : influencingOn) {
            if (cell.isCircleHelper(visited, recStack)) {
                return true; // Cycle detected.
            }
        }

        // Remove this cell from the recursion stack after processing.
        recStack.remove(this);
        return false; // No cycle detected.
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
}