package ca.ulaval.glo4002.reservation.configuration.rest;

import ca.ulaval.glo4002.reservation.configuration.rest.dto.HoppeningDatesDto;
import ca.ulaval.glo4002.reservation.configuration.servable.HoppeningDatesService;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/configuration")
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
  private HoppeningDatesService hoppeningDatesService;

  @Inject
  public ConfigurationResource(HoppeningDatesService hoppeningDatesService) {
    this.hoppeningDatesService = hoppeningDatesService;
  }

  @POST
  public Response changeHoppeningDates(HoppeningDatesDto hoppeningDatesDto) {
    hoppeningDatesService.changeDates(hoppeningDatesDto);
    return Response.ok().build();
  }
}
