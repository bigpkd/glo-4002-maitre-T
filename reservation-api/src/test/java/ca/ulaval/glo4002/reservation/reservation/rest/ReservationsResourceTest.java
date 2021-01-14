package ca.ulaval.glo4002.reservation.reservation.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import ca.ulaval.glo4002.reservation.reservation.domain.ReservationNumber;
import ca.ulaval.glo4002.reservation.reservation.rest.ReservationsResource;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CountryDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.ReservationDetailDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.ReservationRequestDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.TableDto;
import ca.ulaval.glo4002.reservation.reservation.servable.ReservationService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationsResourceTest {
  private static final String VENDOR_CODE = "TEAM";
  private static final String RESPONSE_LOCATION_HEADER = "Location";
  private static final String DINNER_DATE_STRING = "2150-07-21T15:23:20.142Z";
  private static final String DATE_STRING = "2150-05-21T15:23:20.142Z";
  private static final Long ID = 1L;
private static final ReservationNumber NUMBER = ReservationNumber.create(VENDOR_CODE, ID);

  private ReservationsResource reservationsResource;
  private ReservationRequestDto reservationRequestDto;
  private ReservationService reservationService;

  @BeforeEach
  void setUp() {
    CountryDto COUNTRY_DTO = mock(CountryDto.class);
    TableDto TABLE_DTO = mock(TableDto.class);
    var reservationDetailDto = new ReservationDetailDto(COUNTRY_DTO, DATE_STRING);
    List<TableDto> tableDtoList = new ArrayList<>();
    tableDtoList.add(TABLE_DTO);
    reservationRequestDto = new ReservationRequestDto(VENDOR_CODE, DINNER_DATE_STRING, reservationDetailDto, tableDtoList);
    reservationService = mock(ReservationService.class);
    reservationsResource = new ReservationsResource(reservationService);
  }

  @Test
  void whenCreatingReservation_thenCreatedStatusResponseIsObtained() {
    when(reservationService.createReservation(reservationRequestDto)).thenReturn(NUMBER.toString());

    Response response = reservationsResource.createReservation(reservationRequestDto);

    assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
  }

  @Test
  void whenCreatingReservation_thenReservationIsCreated() {
    reservationsResource.createReservation(reservationRequestDto);

    verify(reservationService).createReservation(reservationRequestDto);
  }

  @Test
  void whenCreatingReservationWithReservationDto_thenReservationNumberIsObtained() throws URISyntaxException {
    String expectedLocation = "/reservations/" + NUMBER.toString();
    URI expectedUri = new URI(expectedLocation);
    when(reservationService.createReservation(reservationRequestDto)).thenReturn(NUMBER.toString());

    Response response = reservationsResource.createReservation(reservationRequestDto);

    assertTrue(response.getHeaders().get(RESPONSE_LOCATION_HEADER).contains(expectedUri));
  }

  @Test
  void givenReservationResource_whenFindingReservationWithNumber_ThenReservationServiceFindReservationIsCalled() {
    reservationsResource.getReservation(NUMBER.toString());

    verify(reservationService).findReservation(NUMBER.toString());
  }

  @Test
  void givenReservationResource_whenGetReservationWithNumber_thenOkStatusResponseIsObtained() {
    Response response = reservationsResource.getReservation(NUMBER.toString());

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
  }
}
