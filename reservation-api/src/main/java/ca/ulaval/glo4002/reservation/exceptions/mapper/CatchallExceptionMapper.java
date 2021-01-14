package ca.ulaval.glo4002.reservation.exceptions.mapper;

import ca.ulaval.glo4002.reservation.exceptions.response.ExceptionResponse;
import ca.ulaval.glo4002.reservation.exceptions.InvalidFormatException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CatchallExceptionMapper implements ExceptionMapper<Exception> {
  @Override
  public Response toResponse(Exception exception) {
    InvalidFormatException e = new InvalidFormatException();

    return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionResponse(e.getError(), e.getMessage()))
        .build();
  }
}
