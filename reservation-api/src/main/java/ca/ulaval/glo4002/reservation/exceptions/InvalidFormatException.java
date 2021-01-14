package ca.ulaval.glo4002.reservation.exceptions;

public class InvalidFormatException extends DomainException {
  private static final String message = "Invalid Format";
  private static final String error = "INVALID_FORMAT";

  public InvalidFormatException() {
    super(message);
  }

  public String getError() {
    return error;
  }
}