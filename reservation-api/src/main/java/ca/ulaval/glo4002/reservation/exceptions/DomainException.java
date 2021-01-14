package ca.ulaval.glo4002.reservation.exceptions;

public abstract class DomainException extends RuntimeException {
  public DomainException(String message) {
    super(message);
  }

  public abstract String getError();
}
