package jaxb.schema.xmlprocessing;

// exception class for handling file-related errors during data loading.
public class FileDataException extends Exception{

    public FileDataException(String message) {
        super(message);
    }

    /**
     * Exception thrown when the number of rows in the file is not within the expected range (1 to 50 inclusive).
     */
    public static class InvalidRowCountException extends FileDataException {
        public InvalidRowCountException(int rowCount) {
            super("Invalid number of rows: " + rowCount + ". The number of rows must be between 1 and 50 inclusive.");
        }
    }

    /**
     * Exception thrown when the number of columns in the file is not within the expected range (1 to 25 inclusive).
     */
    public static class InvalidColumnCountException extends FileDataException {
        public InvalidColumnCountException(int columnCount) {
            super("Invalid number of columns: " + columnCount + ". The number of columns must be between 1 and 20 inclusive.");
        }
    }


    /**
     * Exception thrown when the column width is not a positive integer.
     */
    public static class InvalidColumnWidthException extends FileDataException {
        public InvalidColumnWidthException(int columnWidth) {
            super("Invalid column width: " + columnWidth + ". Column width must be a positive integer greater than 0.");
        }
    }

    /**
     * Exception thrown when the row height is not a positive integer.
     */
    public static class InvalidRowHeightException extends FileDataException {
        public InvalidRowHeightException(int rowHeight) {
            super("Invalid row height: " + rowHeight + ". Row height must be a positive integer greater than 0.");
        }
    }

}
