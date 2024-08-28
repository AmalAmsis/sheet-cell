package dto;

import java.util.Map;

/**
 * DTOSheet represents the data transfer object for a spreadsheet.
 * This interface provides methods to retrieve essential information about the sheet,
 * including its version, title, dimensions, and the cells it contains.*/
public interface DTOSheet {

    /**
     * Returns the version number of the sheet.
     * @return an integer representing the sheet's version.*/
    int getSheetVersion();

    /**
     * Returns the title of the sheet.
     * @return a String representing the sheet's title.*/
    String getSheetTitle();

    /**
     * Returns a map of cells in the sheet, with the cell's coordinate as the key.
     * @return a Map where the key is a String representing the cell's coordinate,
     * and the value is a DTOCell object representing the cell.*/
    Map<String,DTOCell> getCells();

    /**
     * Returns the number of rows in the sheet.
     * @return an integer representing the number of rows.*/
    int getNumOfRows();

    /**
     * Returns the number of columns in the sheet.
     * @return an integer representing the number of columns.*/
    int getNumOfColumns();

    /**
     * Returns the height of the rows in the sheet.
     * @return an integer representing the height of the rows.*/
    int getHeightOfRows();

    /**
     * Returns the width of the columns in the sheet.
     * @return an integer representing the width of the columns.*/
    int getWidthOfColumns();
}
