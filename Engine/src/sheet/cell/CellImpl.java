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
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
        this.effectiveValue = new EffectiveValueImpl(CellType.EMPTY, "");
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
        this.originalValue = stlCell.toString();//??????????????????????????????????????????????????????????????
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
        boolean isArgument = false;
        EffectiveValue effectiveValue = ExpressionEvaluator.evaluate(originalValue, sheet, this.coordinate, isArgument);
        return effectiveValue;
    }

    @Override
    public int updateValue(String originalValue) {
        Set<Cell> visitedCells = new HashSet<>();
        try {
            updateValueHelper(originalValue, visitedCells);
            this.originalValue = originalValue;
            for (Cell cell : visitedCells) {
                cell.setLastModifiedVersion(sheet.getVersion());
            }
            return visitedCells.size();
        } catch (Exception e) {
            updateValueHelper(this.originalValue, visitedCells);
            throw e;
        }
    }

    @Override
    public void updateValueHelper(String originalValue, Set<Cell> visitedCells) {
        // הוסיפי את התא הנוכחי לקבוצה אם הוא לא קיים בה כבר
        visitedCells.add(this);
        this.effectiveValue = calculateEffectiveValue(originalValue);
        // עדכני את כל התאים שמושפעים מהתא הנוכחי
        for (Cell cell : influencingOn) {
            cell.updateValueHelper(cell.getOriginalValue(), visitedCells);
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
        Iterator<Cell> iterator = dependsOn.iterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            iterator.remove(); // Safely removes the current element
            cell.removeInfluencingOn(this);
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
        List<Cell> cycle = detectCycle();
        if (cycle != null) {
            this.influencingOn.remove(cell);
            String cyclePath = cycle.stream()
                    .map(Cell:: getId) // Assuming each Cell has a getId implementation
                    .collect(Collectors.joining(" -> "));
            String message = String.format("Cycle detected: %s. REF operation failed due to the cycle.", cyclePath);
            throw new IllegalArgumentException(message);
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

    /**
     * Detects if there is a cycle starting from this cell.
     *
     * @return A list of cells representing the cycle if one is found, otherwise null.
     */
    @Override
    public List<Cell> detectCycle() {
        return detectCycleHelper(new HashSet<>(), new HashSet<>(), new ArrayList<>());
    }

    @Override
    /**
     * Helper method to detect cycles using DFS and return the cycle path.
     * If a cell influences another cell, there is a directed edge (or arc) from the influencing cell to the influenced cell.
     *
     * @param visited Keeps track of fully processed cells (Black Nodes).
     * @param recStack Tracks the current path in DFS (Gray Nodes).
     * @param path Records the current path of cells being visited.
     * @return A list of cells representing the cycle if one is found, otherwise null.
     */
    public List<Cell> detectCycleHelper(Set<Cell> visited, Set<Cell> recStack, List<Cell> path) {
        // If this cell is already in the recursion stack, a back edge (cycle) is detected.
        if (recStack.contains(this)) {
            path.add(this);
            return path;
        }

        // If this cell is already visited, it's fully processed, so no cycle here.
        if (visited.contains(this)) {
            return null;
        }

        // Mark this cell as visited and add it to the recursion stack.
        visited.add(this);
        recStack.add(this);
        path.add(this);

        // Recursively check all influenced cells.
        for (Cell cell : influencingOn) {
            List<Cell> cyclePath = cell.detectCycleHelper(visited, recStack, path);
            if (cyclePath != null) {
                if (cyclePath.get(0) == this) {
                    return cyclePath; // Close the cycle
                } else {
                    return cyclePath;
                }
            }
        }

        // Remove this cell from the recursion stack after processing.
        recStack.remove(this);
        path.remove(path.size() - 1);
        return null; // No cycle detected.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellImpl cell = (CellImpl) o;
        return  Objects.equals(coordinate, cell.coordinate) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }
}