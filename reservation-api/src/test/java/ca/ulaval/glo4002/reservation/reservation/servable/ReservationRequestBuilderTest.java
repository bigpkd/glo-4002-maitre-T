package ca.ulaval.glo4002.reservation.reservation.servable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.reservation.domain.Country;
import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidDinnerDateException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidReservationDateException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CountryDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.ReservationDetailDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.ReservationRequestDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.TableDto;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationRequestBuilderTest {
  private static final String VENDOR_CODE = "TEAM";
  private static final String DINNER_DATE = "2150-07-21T15:23:20.142Z";
  private static final String RESERVATION_DATE = "2150-05-21T15:23:20.142Z";

  private static final String CUSTOMER_NAME = "Name";

  private static final String CANADA_CODE = "CAN";
  private static final String CANADA_FULL_NAME = "Canada";
  private static final String CANADA_CURRENCY = "$CAN";
  private static final Country COUNTRY = new Country("CAN", "Canada", "$CAN");

  private static final List<Restriction> EMPTY_CUSTOMER_RESTRICTIONS = new ArrayList<>();
  private final static List<TableDto> EMPTY_TABLE_DTO_LIST = new ArrayList<>();

  private ReservationRequestBuilder reservationRequestBuilder;
  private TableBuilder tableBuilder;
  private CountryMapper countryMapper;
  private HoppeningDates hoppeningDates;

  private ReservationRequestDto reservationRequestDto;
  private Table table;

  @BeforeEach
  void setUp() {
    tableBuilder = mock(TableBuilder.class);
    countryMapper = mock(CountryMapper.class);
    hoppeningDates = mock(HoppeningDates.class);
    when(hoppeningDates.validateDinnerDate(any())).thenReturn(true);
    when(hoppeningDates.validateReservationDate(any())).thenReturn(true);
    reservationRequestBuilder =
        new ReservationRequestBuilder(tableBuilder, countryMapper, hoppeningDates);

    setUpReservationFactoriesAndReservationRequestDto();
  }

  @Test
  void whenCreatingReservation_thenVendorCodeIsMappedIntoReservationRequest() {
    ReservationRequest reservationRequest = reservationRequestBuilder.create(reservationRequestDto);

    assertEquals(VENDOR_CODE, reservationRequest.getVendorCode());
  }

  @Test
  void whenCreatingReservation_thenDinnerDateIsMappedIntoReservationRequest() {
    OffsetDateTime expectedDinnerDate = OffsetDateTime.parse(DINNER_DATE);

    ReservationRequest reservationRequest = reservationRequestBuilder.create(reservationRequestDto);

    assertEquals(expectedDinnerDate, reservationRequest.getDinnerDate());
  }

  @Test
  void whenCreatingReservation_thenReservationDateIsMappedIntoReservationRequest() {
    OffsetDateTime expectedDate = OffsetDateTime.parse(RESERVATION_DATE);

    ReservationRequest reservation = reservationRequestBuilder.create(reservationRequestDto);

    assertEquals(expectedDate, reservation.getReservationDate());
  }

  @Test
  void whenCreatingReservation_thenCountryIsMappedIntoReservationRequest() {
    ReservationRequest reservationRequest = reservationRequestBuilder.create(reservationRequestDto);

    assertEquals(COUNTRY, reservationRequest.getCountry());
  }

  @Test
  void whenCreatingReservation_thenTablesAreMappedIntoReservationRequest() {
    ReservationRequest reservationRequest = reservationRequestBuilder.create(reservationRequestDto);

    assertTrue(reservationRequest.getTables().contains(table));
  }

  @Test
  void whenCreatingReservationWithoutTables_thenInvalidReservationQuantityExceptionIsThrown() {
    reservationRequestDto.tables = EMPTY_TABLE_DTO_LIST;

    assertThrows(InvalidReservationQuantityException.class, () -> reservationRequestBuilder.create(reservationRequestDto));
  }

  @Test
  void givenInvalidDinnerDate_thenInvalidDinnerDateIsThrown(){
    when(hoppeningDates.validateDinnerDate(any())).thenReturn(false);

    assertThrows(InvalidDinnerDateException.class, () -> reservationRequestBuilder.create(reservationRequestDto));
  }

  @Test
  void givenInvalidReservationDate_thenInvalidReservationDateIsThrown(){
    when(hoppeningDates.validateReservationDate(any())).thenReturn(false);

    assertThrows(InvalidReservationDateException.class, () -> reservationRequestBuilder.create(reservationRequestDto));
  }


  private void setUpReservationFactoriesAndReservationRequestDto() {
    CountryDto countryDto = new CountryDto(CANADA_CODE, CANADA_FULL_NAME, CANADA_CURRENCY);

    ReservationDetailDto reservationDetailDto = new ReservationDetailDto(countryDto, RESERVATION_DATE);

    CustomerDto customerDto = new CustomerDto(CUSTOMER_NAME, new ArrayList<>());

    List<CustomerDto> customerDtoList = new ArrayList<>();
    customerDtoList.add(customerDto);
    TableDto tableDto = new TableDto(customerDtoList);

    List<TableDto> tablesDto = new ArrayList<>();
    tablesDto.add(tableDto);
    reservationRequestDto = new ReservationRequestDto(VENDOR_CODE, DINNER_DATE,
                                                      reservationDetailDto, tablesDto);

    Customer customer = new Customer(CUSTOMER_NAME, EMPTY_CUSTOMER_RESTRICTIONS, new ArrayList<>());
    List<Customer> customers = new ArrayList<>();
    customers.add(customer);
    table = new Table(customers);

    when(tableBuilder.create(tableDto)).thenReturn(table);
    when(countryMapper.toCountry(countryDto)).thenReturn(COUNTRY);
  }

}