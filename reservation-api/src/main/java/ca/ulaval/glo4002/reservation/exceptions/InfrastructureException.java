package ca.ulaval.glo4002.reservation.exceptions;

public abstract class InfrastructureException extends RuntimeException {
  public InfrastructureException(String message) {
    super(message);
  }

  public abstract String getError();
}
