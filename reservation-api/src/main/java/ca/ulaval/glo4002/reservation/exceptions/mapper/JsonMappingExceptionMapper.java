package ca.ulaval.glo4002.reservation.exceptions.mapper;

import ca.ulaval.glo4002.reservation.exceptions.response.ExceptionResponse;
import ca.ulaval.glo4002.reservation.exceptions.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {
  @Override
  public Response toResponse(UnrecognizedPropertyException exception) {
    InvalidFormatException e = new InvalidFormatException();

    return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionResponse(e.getError(), e.getMessage()))
        .build();
  }
}