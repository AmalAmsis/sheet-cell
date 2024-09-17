package sheet.sortsheet;

import sheet.coordinate.Coordinate;

/**
 * RangeValidationException is a custom exception class for handling errors related to range validation in the spreadsheet.
 * This class provides specific nested exceptions for different types of validation errors
 * encountered when processing ranges and coordinates.
 */
public class RangeValidationException extends Exception {

  /** Constructs a RangeValidationException with the specified detail message.
   * @param message the detail message. */
  public RangeValidationException(String message) {
    super(message);
  }

  /**
   * Exception thrown when the 'from' coordinate is after the 'to' coordinate.
   */
  public static class InvalidRangeOrderException extends RangeValidationException {
    public InvalidRangeOrderException(Coordinate from, Coordinate to) {
      super("'From' coordinate " + from.toString() + " must be before 'To' coordinate " + to.toString() + ".");
    }
  }

  /**
   * Exception thrown when the coordinates are outside the boundaries of the sheet.
   */
  public static class CoordinateOutOfBoundsException extends RangeValidationException {
    public CoordinateOutOfBoundsException(Coordinate coord) {
      super("Coordinate " + coord + " is outside the sheet's boundaries.");
    }
  }

  /**
   * Exception thrown when a column is selected that does not contain numeric values.
   */
  public static class NonNumericColumnException extends RangeValidationException {
    public NonNumericColumnException(char column) {
      super("Column " + column + " does not contain only numeric values.");
    }
  }

//  /**
//   * Exception thrown when the range specified is empty (no cells within the range).
//   */
//  public static class EmptyRangeException extends RangeValidationException {
//    public EmptyRangeException() {
//      super("The specified range is empty. No cells to process.");
//    }
//  }

}
