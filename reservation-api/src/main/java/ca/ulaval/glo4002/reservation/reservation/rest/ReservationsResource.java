package ca.ulaval.glo4002.reservation.reservation.rest;

import ca.ulaval.glo4002.reservation.reservation.rest.dto.ReservationRequestDto;
import ca.ulaval.glo4002.reservation.reservation.rest.response.ReservationResponse;
import ca.ulaval.glo4002.reservation.reservation.servable.ReservationService;
import java.net.URI;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationsResource {
  private ReservationService reservationService;

  @Inject
  public ReservationsResource(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GET
  @Path("/{reservation_number}")
  public Response getReservation(@PathParam("reservation_number") String reservationNumber) {
    ReservationResponse reservationResponse = reservationService.findReservation(reservationNumber);
    return Response.status(Response.Status.OK).entity(reservationResponse).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createReservation(@Valid ReservationRequestDto reservationRequestDto) {
    String reservationId = reservationService.createReservation(reservationRequestDto);
    return Response.created(URI.create("/reservations/" + reservationId)).build();
  }
}