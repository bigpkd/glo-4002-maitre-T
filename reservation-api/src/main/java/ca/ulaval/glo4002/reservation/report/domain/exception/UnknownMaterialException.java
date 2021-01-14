package ca.ulaval.glo4002.reservation.report.domain.exception;

import ca.ulaval.glo4002.reservation.exceptions.DomainException;

public class UnknownMaterialException extends DomainException {

  private static final String message = "The material you are trying to create is unknown to the system";
  private static final String error = "UNKNOWN_MATERIAL_EXCEPTION";

  public UnknownMaterialException() {
    super(message);
  }

  public String getError() {
    return error;
  }

}
