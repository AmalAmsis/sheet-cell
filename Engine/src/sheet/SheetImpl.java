package sheet;

import dto.DTOSheet;
import dto.DTOSheetImpl;
import sheet.coordinate.CoordinateImpl;
import sheet.effectivevalue.EffectiveValue;
import sheet.cell.Cell;
import sheet.cell.CellImpl;
import sheet.coordinate.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {

    private int version;
    private final String title;
    private final Map<String, Cell> board;
    private final int numOfRows;
    private final int numOfCols;
    private final int heightOfRows;
    private final int widthOfCols;

    // Constructor
    public SheetImpl(String title, int numOfRows, int numOfCols, int heightOfRows, int widthOfCols) {
        this.version = 0;
        this.title = title;
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        this.heightOfRows = heightOfRows;
        this.widthOfCols = widthOfCols;
        this.board = new HashMap<>();
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
    public Cell getCell(Coordinate coordinate) {
        return board.get(coordinate.toString());
    }

    @Override
    public void setCell(Coordinate coordinate, String originalValue) {
        updateVersion(); // Every change in a cell updates the sheet version.

        if (originalValue.isBlank()) { // If the original value is blank
            removeCell(coordinate); // Remove the cell if the value is blank.
            return;
        }

        EffectiveValue effectiveValue = calculateEffectiveValue(originalValue);

        if (!isCellInSheet(coordinate)) {
            addCell(coordinate, originalValue, effectiveValue);
        } else {
            updateCell(coordinate, originalValue, effectiveValue);
        }
    }

    // Update the sheet version
    private void updateVersion() {
        version++;
    }

    // Check if a cell exists in the sheet
    public boolean isCellInSheet(Coordinate coordinate) {
        return board.containsKey(coordinate.toString());
    }

    @Override
    public EffectiveValue getCellEffectiveValue(Coordinate coordinate) {
        if (isCellInSheet(coordinate)) {
            return board.get(coordinate.toString()).getEffectiveValue();
        }
        return null;
    }

    // Add a new cell to the sheet
    public void addCell(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue) {
        Cell myCell = new CellImpl(coordinate, originalValue, effectiveValue);
        board.put(coordinate.toString(), myCell);
    }

    // Update an existing cell in the sheet
    public void updateCell(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue) {
        Cell myCell = board.get(coordinate.toString());
        if (myCell != null) {
            myCell.setOriginalValue(originalValue);
            myCell.setLastModifiedVersion(version);
        }
    }

    // Remove a cell from the sheet
    public void removeCell(Coordinate coordinate) {
        board.remove(coordinate.toString());
    }

    // Convert the sheet to a DTO for data transfer
    public DTOSheet convertToDTOSheet() {
        DTOSheet dtoSheet = new DTOSheetImpl();
        dtoSheet.setVersion(version);
        dtoSheet.setTitle(title);
        dtoSheet.setNumOfRows(numOfRows);
        dtoSheet.setNumOfColumns(numOfCols);
        dtoSheet.setHeightOfRows(heightOfRows);
        dtoSheet.setWidthOfColumns(widthOfCols);
        for (Cell cell : board.values()) {
            dtoSheet.addDTOCell(cell.convertToDTOCell());
        }
        return dtoSheet;
    }

}
