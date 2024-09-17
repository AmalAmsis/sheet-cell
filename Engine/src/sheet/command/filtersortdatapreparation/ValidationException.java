package sheet.command.filtersortdatapreparation;

import sheet.coordinate.Coordinate;

/**
 * RangeValidationException is a custom exception class for handling errors related to range validation in the spreadsheet.
 * This class provides specific nested exceptions for different types of validation errors
 * encountered when processing ranges and coordinates.
 */
public class ValidationException extends Exception {

  /** Constructs a RangeValidationException with the specified detail message.
   * @param message the detail message. */
  public ValidationException(String message) {
    super(message);
  }

  /**
   * Exception thrown when the 'from' coordinate is after the 'to' coordinate.
   */
  public static class InvalidRangeOrderException extends ValidationException {
    public InvalidRangeOrderException(Coordinate from, Coordinate to) {
      super("The 'From' coordinate (" + from.toString() + ") must have a column letter\n" +
              " that is the same or " + "or comes before the 'To' coordinate's (" + to.toString() + ").\n" +
              " Similarly, " + "the row number of the 'From' coordinate must be\n" +
              " the same or smaller than the 'To' coordinate.\n\n");

    }
  }

  /**
   * Exception thrown when the coordinates are outside the boundaries of the sheet.
   */
  public static class CoordinateOutOfBoundsException extends ValidationException {
    public CoordinateOutOfBoundsException(Coordinate coord) {
      super("At list one of the coordinate is outside the valid boundaries of the sheet.\n" +
              " Please select a coordinate within the sheet.");
    }
  }

  /**
   * Exception thrown when a column is selected that does not contain numeric values.
   */
  public static class NonNumericColumnException extends ValidationException {
    public NonNumericColumnException(char column) {
      super("One or more columns contain non-numeric values.\n All columns selected for sorting must contain only numeric values.");
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
