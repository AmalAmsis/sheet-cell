package sheet;

import jaxb.schema.generated.STLSheet;
import sheet.cell.Cell;
import sheet.coordinate.Coordinate;


/**
 * Sheet represents the interface for a spreadsheet.
 * It extends SheetDataModifier, SheetDataRetriever, and SheetDependencyManager,
 * combining functionalities for modifying data, retrieving data, and managing dependencies.
 */
public interface Sheet extends SheetDataModifier, SheetDataRetriever, SheetDependencyManager {

    /** Retrieves the cell at the specified coordinate.
     * @param coordinate the coordinate of the cell to retrieve.
     * @return the Cell object at the specified coordinate, or null if no cell exists at that position. */
    Cell getCell(Coordinate coordinate);


    //todo : להעביר את התיעוד למימוש ולמחוק מהinterface
    //not in was right now - maybe we will need it in te future
    /** Converts the current state of the sheet into an STLSheet object for serialization.
     * @return an STLSheet object representing the current state of the sheet. */
    STLSheet convertFromSheetToStlSheet();

    /** Converts a string representation of a coordinate to a Coordinate object.
     * The string should be in the format where the first character represents the column
     * and the following characters represent the row number (e.g., "A1", "B2").
     * @param stringCoordinate the string representation of the coordinate.
     * @return a Coordinate object corresponding to the provided string.
     * @throws NumberFormatException if the row part of the string is not a valid integer.
     * @throws StringIndexOutOfBoundsException if the stringCoordinate is too short to contain both a column and a row. */
    Coordinate convertStringToCoordinate(String stringCoordinate);
}
