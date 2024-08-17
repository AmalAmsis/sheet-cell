package sheet;

import dto.DTOCell;
import dto.DTOCoordinate;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.effectivevalue.EffectiveValue;
import sheet.cell.Cell;
import sheet.cell.CellImpl;
import sheet.coordinate.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {

    int version;
    String title;
    Map<String, Cell> board = new HashMap<>();
    final int numOfRows;
    final int numOfCols;
    final int heightOfRows;
    final int widthOfCols;

    //CTOR
    public SheetImpl(String title, int numOfRows, int numOfCols, int heightOfRows, int widthOfCols) {
        this.version = 0;
        this.title = title;
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        this.heightOfRows = heightOfRows;
        this.widthOfCols = widthOfCols;
    }

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
      return board.get(coordinate.toString());
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
        Cell myCell = new CellImpl(coordinate, originalValue,effectiveValue);
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

    public DTOSheet convertToDTOSheet(){
        DTOSheet dtoSheet = new DTOSheetImpl();
        dtoSheet.setVersion(version);
        dtoSheet.setTitle(title);
        dtoSheet.setNumOfRows(numOfRows);
        dtoSheet.setNumOfColumns(numOfCols);
        dtoSheet.setHeightOfRows(heightOfRows);
        dtoSheet.setWidthOfColumns(widthOfCols);
        for( Cell cell : board.values()) {
            dtoSheet.addDTOCell(cell.convertToDTOCell());
         }
        return dtoSheet;
    }

//    @Override
//    public void addCell(DTOCell cell) {
//        DTOCoordinate coordinate = cell.getCoordinate();
//        board.put(coordinate.toString(), cell);
//    }
}
