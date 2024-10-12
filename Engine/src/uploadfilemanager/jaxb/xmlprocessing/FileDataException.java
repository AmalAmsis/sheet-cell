package uploadfilemanager.jaxb.xmlprocessing;

/**
 * FileDataException is a custom exception class for handling file-related errors during data loading.
 * This class provides specific nested exceptions for different types of validation errors
 * encountered when processing spreadsheet files.
 */
public class FileDataException extends Exception{

    /** Constructs a FileDataException with the specified detail message.
     * @param message the detail message. */
    public FileDataException(String message) {
        super(message);
    }

    /**
     * Exception thrown when the number of rows in the file is not within the expected range (1 to 50 inclusive).*/
    public static class InvalidRowCountException extends FileDataException {
        public InvalidRowCountException(int rowCount) {
            super("The XML file specifies an invalid number of rows: " + rowCount
                    + ".\nPlease ensure the sheet has between 1 and 50 rows.");
        }
    }

    /**
     * Exception thrown when the number of columns in the file is not within the expected range (1 to 25 inclusive).*/
    public static class InvalidColumnCountException extends FileDataException {
        public InvalidColumnCountException(int columnCount) {
            super("The XML file specifies an invalid number of columns: " + columnCount
            + ".\nPlease ensure the sheet has between 1 and 20 columns.");
        }
    }


    /**
     * Exception thrown when the column width is not a positive integer.*/
    public static class InvalidColumnWidthException extends FileDataException {
        public InvalidColumnWidthException(int columnWidth) {
            super(" The XML file specifies an invalid column width: " + columnWidth
            + ".\nPlease ensure the column width is a positive number.");
        }
    }

    /**
     * Exception thrown when the row height is not a positive integer.*/
    public static class InvalidRowHeightException extends FileDataException {
        public InvalidRowHeightException(int rowHeight) {
            super("The XML file specifies an invalid row height: " + rowHeight
                    + ".\nPlease ensure the row width is a positive number.");
        }
    }

    /**
     * Exception thrown when a cell in the file is outside the bounds of the sheet.*/
    public static class CellOutOfBoundsException extends FileDataException {
        public CellOutOfBoundsException() {
            super("The file contains one or more cells that are positioned outside the valid sheet boundaries. " +
                    "\nPlease ensure all cells in the file are within the defined grid of the sheet.");
        }
    }

    /**
     * Exception thrown when a circular reference is detected in the cell graph.*/
    public static class CircularReferenceException extends FileDataException {
        public CircularReferenceException() {
            super("The file contains a circular reference, which is not allowed.");
        }
    }

}
