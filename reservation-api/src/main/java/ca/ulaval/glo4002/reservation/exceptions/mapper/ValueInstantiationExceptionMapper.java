package ca.ulaval.glo4002.reservation.exceptions.mapper;

import ca.ulaval.glo4002.reservation.configuration.exception.InvalidDateException;
import ca.ulaval.glo4002.reservation.exceptions.response.ExceptionResponse;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValueInstantiationExceptionMapper implements ExceptionMapper<ValueInstantiationException> {
  @Override
  public Response toResponse(ValueInstantiationException e) {
    InvalidDateException exception = new InvalidDateException();
    ExceptionResponse error = new ExceptionResponse(exception.getError(), exception.getMessage());

    return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
  }
}
