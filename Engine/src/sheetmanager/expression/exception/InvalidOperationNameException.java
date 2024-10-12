package sheetmanager.expression.exception;

public class InvalidOperationNameException extends IllegalArgumentException {
    public InvalidOperationNameException(String operationName) {
        super("Operation '" + operationName + "' is not a valid operation. Please provide a valid operation name.");
    }
}
