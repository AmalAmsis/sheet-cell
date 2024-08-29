package jaxb.schema.xmlprocessing;

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
            super("Invalid number of rows: " + rowCount);
        }
    }

    /**
     * Exception thrown when the number of columns in the file is not within the expected range (1 to 25 inclusive).*/
    public static class InvalidColumnCountException extends FileDataException {
        public InvalidColumnCountException(int columnCount) {
            super("Invalid number of columns: " + columnCount);
        }
    }


    /**
     * Exception thrown when the column width is not a positive integer.*/
    public static class InvalidColumnWidthException extends FileDataException {
        public InvalidColumnWidthException(int columnWidth) {
            super("Invalid column width: " + columnWidth);
        }
    }

    /**
     * Exception thrown when the row height is not a positive integer.*/
    public static class InvalidRowHeightException extends FileDataException {
        public InvalidRowHeightException(int rowHeight) {
            super("Invalid row height: " + rowHeight);
        }
    }

    /**
     * Exception thrown when a cell in the file is outside the bounds of the sheet.*/
    public static class CellOutOfBoundsException extends FileDataException {
        public CellOutOfBoundsException() {
            super("There is a cell outside the borders of the sheet.");
        }
    }

    /**
     * Exception thrown when a circular reference is detected in the cell graph.*/
    public static class CircularReferenceException extends FileDataException {
        public CircularReferenceException() {
            super("Circular reference detected in the cell graph.");
        }
    }

}
