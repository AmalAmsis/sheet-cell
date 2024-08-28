package dto;

import java.util.List;

/**
 * DTOCell represents the data transfer object for a cell in a spreadsheet.
 * This interface provides methods to retrieve essential information about the cell.
 * It is used to transfer data between the engine and the UI without exposing the underlying data model.
 */

public interface DTOCell {

    /**
     * Returns the coordinate of the cell.
     * @return the DTOCoordinate representing the cell's position.*/
    DTOCoordinate getCoordinate();

    /**
     * Returns the effective value of the cell after all calculations and dependencies are resolved.
     * @return a String representing the effective value of the cell.*/
    String getEffectiveValue();

    /**
     * Returns the original value of the cell before any calculations.
     * @return a String representing the original value of the cell.*/
    String getOriginalValue();

    /**
     * Returns the version number of the last modification made to the cell.
     * @return an integer representing the last modified version.*/
    int getLastModifiedVersion();

    /**
     * Returns a list of coordinates representing the cells that this cell depends on.
     * @return a List of DTOCoordinate objects that this cell depends on.*/
    List<DTOCoordinate> getDependsOn();

    /**
     * Returns a list of coordinates representing the cells that depend on this cell.
     * @return a List of DTOCoordinate objects that are influenced by this cell.*/
    List<DTOCoordinate> getInfluencingOn();

}
