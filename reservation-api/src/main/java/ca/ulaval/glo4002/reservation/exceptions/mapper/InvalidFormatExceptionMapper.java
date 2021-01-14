package ca.ulaval.glo4002.reservation.exceptions.mapper;

import ca.ulaval.glo4002.reservation.exceptions.InvalidFormatException;
import ca.ulaval.glo4002.reservation.exceptions.response.ExceptionResponse;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidFormatExceptionMapper implements ExceptionMapper<ValidationException> {
  @Override
  public Response toResponse(ValidationException exception) {
    InvalidFormatException invalidFormatException = new InvalidFormatException();
    ExceptionResponse error =
        new ExceptionResponse(invalidFormatException.getError(), invalidFormatException.getMessage());

    return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
  }
}