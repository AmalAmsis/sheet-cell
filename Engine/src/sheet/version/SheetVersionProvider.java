package sheet.version;

import dto.DTOSheet;

/**
 * SheetVersionProvider provides access to specific versions of a spreadsheet.
 * This interface allows retrieving a Data Transfer Object (DTO) representation
 * of a sheet based on a given version number.
 */
public interface SheetVersionProvider  {

    /** Retrieves the DTO representation of the sheet for a specific version.
     * @param version the version number of the sheet to retrieve.
     * @return a DTOSheet object representing the sheet's state at the specified version.
     * @throws IllegalArgumentException if the version number is invalid.*/
    DTOSheet getSheetByVersion(int version);
}
