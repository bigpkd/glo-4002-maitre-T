package ca.ulaval.glo4002.reservation.exceptions;

public abstract class RestException extends RuntimeException {
  public RestException(String message) {
    super(message);
  }

  public abstract String getError();
}
