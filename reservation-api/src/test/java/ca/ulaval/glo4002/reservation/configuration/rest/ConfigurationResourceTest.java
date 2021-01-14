package ca.ulaval.glo4002.reservation.configuration.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


import ca.ulaval.glo4002.reservation.configuration.rest.dto.HoppeningDatesDto;
import ca.ulaval.glo4002.reservation.configuration.servable.HoppeningDatesService;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfigurationResourceTest {
  private HoppeningDatesDto hoppeningDatesDto;
  private HoppeningDatesService hoppeningDatesService;
  private ConfigurationResource configurationResource;

  @BeforeEach
  void setupHoppeningDatesResource() {
    hoppeningDatesDto = mock(HoppeningDatesDto.class);
    hoppeningDatesService = mock(HoppeningDatesService.class);
    configurationResource = new ConfigurationResource(hoppeningDatesService);
  }

  @Test
  void whenChangingHoppeningDates_thenShouldCallHoppeningDatesServiceToChangeDates() {
    configurationResource.changeHoppeningDates(hoppeningDatesDto);

    verify(hoppeningDatesService).changeDates(hoppeningDatesDto);
  }

  @Test
  void whenChangingHoppeningDates_thenShouldObtainOkResponseStatus() {
    Response response = configurationResource.changeHoppeningDates(hoppeningDatesDto);

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
  }
}
