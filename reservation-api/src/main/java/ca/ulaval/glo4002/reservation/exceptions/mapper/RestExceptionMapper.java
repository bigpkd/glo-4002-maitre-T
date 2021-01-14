package ca.ulaval.glo4002.reservation.exceptions.mapper;

import ca.ulaval.glo4002.reservation.exceptions.RestException;
import ca.ulaval.glo4002.reservation.exceptions.response.ExceptionResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper implements ExceptionMapper<RestException> {
  @Override
  public Response toResponse(RestException exception) {
    ExceptionResponse error = new ExceptionResponse(exception.getError(), exception.getMessage());

    return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
  }
}
