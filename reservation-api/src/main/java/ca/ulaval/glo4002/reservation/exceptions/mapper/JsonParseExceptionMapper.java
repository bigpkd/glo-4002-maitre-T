package ca.ulaval.glo4002.reservation.exceptions.mapper;

import ca.ulaval.glo4002.reservation.exceptions.response.ExceptionResponse;
import ca.ulaval.glo4002.reservation.exceptions.InvalidFormatException;
import com.fasterxml.jackson.core.JsonParseException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {
  @Override
  public Response toResponse(JsonParseException exception) {
    InvalidFormatException e = new InvalidFormatException();

    return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionResponse(e.getError(), e.getMessage()))
        .build();
  }
}
