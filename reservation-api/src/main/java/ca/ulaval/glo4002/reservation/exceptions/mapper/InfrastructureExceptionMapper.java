package ca.ulaval.glo4002.reservation.exceptions.mapper;

import ca.ulaval.glo4002.reservation.exceptions.InfrastructureException;
import ca.ulaval.glo4002.reservation.exceptions.response.ExceptionResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InfrastructureExceptionMapper implements ExceptionMapper<InfrastructureException> {
  @Override
  public Response toResponse(InfrastructureException exception) {
    ExceptionResponse error = new ExceptionResponse(exception.getError(), exception.getMessage());

    return Response.status(Response.Status.NOT_FOUND).entity(error).build();
  }
}
