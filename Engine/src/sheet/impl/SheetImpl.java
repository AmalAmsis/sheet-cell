package sheet.impl;

import sheet.api.EffectiveValue;
import sheet.api.Sheet;
import sheet.cell.Cell;
import sheet.cell.CellImpl;
import sheet.coordinate.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {

    int version;
    String title;
    Map<String, Cell> board = new HashMap<>();


    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Cell getCell(Coordinate coordinate) { // to do
        return null;
    }

    @Override
    public void setCell(Coordinate coordinate, String originalValue) {
        updateVersion(); //every change in cell mean that the version of the sheet change.

        if(originalValue.isBlank()) //if the original value contain only white spaces
       {
           removeCell(coordinate); //Whether the cell existed before or not, the result will be the same, there will no longer be this cell.
           return;
       }
        //*AMAL* to do
        EffectiveValue effectiveValue = calculateEffectiveValue(originalValue);

        if(!isCellInSheet(coordinate)) {
            addCell(coordinate, originalValue,effectiveValue);
        }
        else{
            updateCell(coordinate,originalValue,effectiveValue);
        }
    }

    //*AMAL* to do
    public EffectiveValue calculateEffectiveValue(String originalValue) {
        return null;
    }

    public void updateVersion(){
    version++;
    }

    //this func check if the sheet contain the coordinate
    public boolean isCellInSheet(Coordinate coordinate) {
        String key = coordinate.toString();
        return board.containsKey(key);
    }

    //add new cell to our board
    public void addCell(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue) {
        Cell myCell = new CellImpl(coordinate.toString(), originalValue,effectiveValue);
        board.put(coordinate.toString(), myCell);

    }

    //update cell data.
    public void updateCell(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue) {
        Cell myCell = board.get(coordinate.toString());
        if(myCell != null) {
            myCell.setOriginalValue(originalValue);
            myCell.setLastModifiedVersion(version);
        }
    }

    public void removeCell(Coordinate coordinate) {
        board.remove(coordinate.toString());
    }


}
