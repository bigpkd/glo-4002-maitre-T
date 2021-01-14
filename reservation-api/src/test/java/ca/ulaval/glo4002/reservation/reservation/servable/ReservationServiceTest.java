package ca.ulaval.glo4002.reservation.reservation.servable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.hoppening.servable.HoppeningDayService;
import ca.ulaval.glo4002.reservation.reservation.domain.*;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.TooManyPeopleException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.TooPickyException;
import ca.ulaval.glo4002.reservation.reservation.repository.exceptions.ReservationNotFoundException;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CountryDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.ReservationDetailDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.ReservationRequestDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.TableDto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationServiceTest {
  private static final ReservationNumber NUMBER = ReservationNumber.create("A CODE", 1L);
  private static final ReservationNumber NOT_FOUND_NUMBER = ReservationNumber.create("A CODE", 2L);

  private static final String VENDOR_CODE = "VENDOR_CODE";
  private static final String CUSTOMER_NAME = "CUSTOMER_NAME";
  private static final String CARROTS = "Carrots";
  private static final String TOMATO = "Tomato";

  private static final OffsetDateTime ANY_HOPPENING_VALID_DATE = OffsetDateTime.parse("2150-07-27T15:23:20.142Z");
  private static final OffsetDateTime RESERVATION_DATE = OffsetDateTime.parse("2150-05-21T15:23:20.142Z");
  private static final OffsetDateTime HOPPENING_FIRST_DAY = OffsetDateTime.parse("2150-07-20T15:23:20.142Z");
  private static final OffsetDateTime HOPPENING_NINTH_DAY = OffsetDateTime.parse("2150-07-29T15:23:20.142Z");
  private static final LocalDate ANY_HOPPENING_VALID_LOCAL_DATE = ANY_HOPPENING_VALID_DATE.toLocalDate();

  private static final Country CANADA_COUNTRY = new Country("CA", "CANADA", "$CAD");

  private static final String ALLERGIES_RESTRICTION_NAME = "allergies";
  private static final List<Restriction> EMPTY_RESTRICTIONS = new ArrayList<>();

  private static final Customer CUSTOMER_WITHOUT_RESTRICTIONS =
      new Customer(CUSTOMER_NAME, EMPTY_RESTRICTIONS, new ArrayList<>());

  private static final Table TABLE_WITHOUT_RESTRICTIONS = new Table(Arrays.asList(CUSTOMER_WITHOUT_RESTRICTIONS));
  private static final List<Table> TABLES = Arrays.asList(TABLE_WITHOUT_RESTRICTIONS);

  private ReservationService reservationService;
  private ReservationRepository reservationRepository;
  private ReservationRequest reservationRequest;
  private ReservationRequest otherReservationRequest;
  private ReservationFactory reservationFactory;
  private ReservationAssembler reservationAssembler;
  private HoppeningDayService hoppeningDayService;
  private ReservationRequestDto reservationRequestDto;
  private Reservation reservation;
  private ReservationRequestBuilder reservationRequestBuilder;
  private HoppeningDates hoppeningDates;

  @BeforeEach
  public void setUp() {
    reservationRepository = mock(ReservationRepository.class);
    reservationFactory = mock(ReservationFactory.class);
    reservationAssembler = mock(ReservationAssembler.class);
    hoppeningDayService = mock(HoppeningDayService.class);
    reservationRequestBuilder = mock(ReservationRequestBuilder.class);
    reservationService = new ReservationService(reservationRepository, reservationFactory,
        reservationAssembler, hoppeningDayService, reservationRequestBuilder);

    reservationRequest = mock(ReservationRequest.class);
    reservationRequestDto = createReservationRequestDto();
    reservation = mock(Reservation.class);

    when(reservationFactory.create(reservationRequest)).thenReturn(reservation);
    when(reservationRepository.getReservation(NUMBER)).thenReturn(reservation);


    otherReservationRequest = new ReservationRequest(VENDOR_CODE, ANY_HOPPENING_VALID_DATE, RESERVATION_DATE,
        CANADA_COUNTRY, TABLES);
    int ZERO_CUSTOMER = 0;
    when(hoppeningDayService.getCustomerCountForDate(any())).thenReturn(ZERO_CUSTOMER);
    when(hoppeningDayService.checkIsWithinFiveFirstHoppeningDay(any())).thenReturn(false);
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(otherReservationRequest);

    hoppeningDates = mock(HoppeningDates.class);
    when(hoppeningDayService.getHoppeningDates()).thenReturn(hoppeningDates);

    when(reservationRequest.getDinnerDate()).thenReturn(ANY_HOPPENING_VALID_DATE);

  }

  @Test
  void whenCreatingReservation_thenReservationFactoryShouldCreateReservation() {
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(reservationRequest);

    reservationService.createReservation(reservationRequestDto);

    verify(reservationFactory).create(reservationRequest);
  }

  @Test
  void whenCreatingReservation_thenReservationRepositoryShouldStoreReservation() {
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(reservationRequest);

    reservationService.createReservation(reservationRequestDto);

    verify(reservationRepository).store(reservation);
  }

  @Test
  void whenCreatingReservation_thenHoppeningDayServiceShouldRegisterReservation() {
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(reservationRequest);

    reservationService.createReservation(reservationRequestDto);

    verify(hoppeningDayService).registerReservation(reservation);
  }

  @Test
  void whenCreatingReservation_thenReservationAssemblerShouldCreateResponse() {
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(reservationRequest);

    reservationService.createReservation(reservationRequestDto);

    verify(reservationAssembler).toReservationStringNumber(reservation);
  }

  @Test
  void givenNonStoredReservation_whenFindingReservation_thenReturnReservationNotFoundExceptionIsThrown() {
    assertThrows(ReservationNotFoundException.class,
        () -> reservationService.findReservation(NOT_FOUND_NUMBER.toString()));
  }

  @Test
  void givenAReservationNumber_whenFindingReservation_thenReservationRepositoryShouldFetchReservation() {
    reservationService.findReservation(NUMBER.toString());

    verify(reservationRepository).getReservation(NUMBER);
  }

  @Test
  void whenCreatingReservationRequest_thenReservationRequestBuilderShouldCallToCreateReservationRequest() {
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(otherReservationRequest);

    reservationService.createReservationRequest(reservationRequestDto);

    verify(reservationRequestBuilder).create(reservationRequestDto);
  }

  @Test
  void whenCreatingReservationRequest_thenReservationRequestShouldBeObtained() {
    ReservationRequest aReservationRequest = reservationService.createReservationRequest(reservationRequestDto);

    assertEquals(otherReservationRequest, aReservationRequest);
  }


  @Test
  void whenCreatingReservationWithEnoughCustomersToBreachDailyLimit_thenTooManyPeopleExceptionIsThrown() {
    int theMaximumNumberOfCustomerPerDay = 42;
    when(hoppeningDayService.getCustomerCountForDate(any()))
        .thenReturn(theMaximumNumberOfCustomerPerDay);

    assertThrows(TooManyPeopleException.class, () -> reservationService.createReservationRequest(reservationRequestDto));
  }


  @Test
  void givenADateWithAllergiesRestrictionReserved_whenCreateReservationIncludingCarrotsIngredient_thenTooPickyExceptionIsThrown() {
    otherReservationRequest = mock(ReservationRequest.class);
    when(otherReservationRequest.countCustomers()).thenReturn(0);
    when(otherReservationRequest.getDinnerDate()).thenReturn(ANY_HOPPENING_VALID_DATE);
    when(otherReservationRequest.containsIngredient(CARROTS)).thenReturn(true);
    when(hoppeningDayService
        .restrictionExistsForHoppeningDay(ANY_HOPPENING_VALID_DATE, ALLERGIES_RESTRICTION_NAME))
        .thenReturn(true);
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(otherReservationRequest);

    assertThrows(TooPickyException.class, () -> reservationService.createReservationRequest(reservationRequestDto));
  }

  @Test
  void givenADateWithAllergiesRestrictionReserved_whenCreateReservationNotIncludingCarrotsIngredient_thenReservationRequestShouldBeObtained() {
    otherReservationRequest = mock(ReservationRequest.class);
    when(otherReservationRequest.countCustomers()).thenReturn(0);
    when(otherReservationRequest.getDinnerDate()).thenReturn(ANY_HOPPENING_VALID_DATE);
    when(otherReservationRequest.containsIngredient(CARROTS)).thenReturn(false);
    when(hoppeningDayService.restrictionExistsForHoppeningDay(ANY_HOPPENING_VALID_DATE, CARROTS))
        .thenReturn(false);
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(otherReservationRequest);

    ReservationRequest aReservationRequest = reservationService.createReservationRequest(reservationRequestDto);

    assertEquals(aReservationRequest, otherReservationRequest);
  }

  @Test
  void givenADateWithCarrotsMenuReserved_whenCreateReservationIncludingAllergiesRestriction_thenTooPickyExceptionIsThrown() {
    otherReservationRequest = mock(ReservationRequest.class);
    when(otherReservationRequest.countCustomers()).thenReturn(0);
    when(otherReservationRequest.getDinnerDate()).thenReturn(ANY_HOPPENING_VALID_DATE);
    when(otherReservationRequest.containsRestriction(ALLERGIES_RESTRICTION_NAME)).thenReturn(true);
    when(hoppeningDayService.ingredientExistsForHoppeningDay(ANY_HOPPENING_VALID_DATE, CARROTS))
        .thenReturn(true);
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(otherReservationRequest);

    assertThrows(TooPickyException.class, () -> reservationService.createReservationRequest(reservationRequestDto));
  }

  @Test
  void givenADateWithCarrotsMenuReserved_whenCreateReservationNotIncludingAllergiesRestriction_thenNewReservationIdObtained() {
    otherReservationRequest = mock(ReservationRequest.class);
    when(otherReservationRequest.countCustomers()).thenReturn(0);
    when(otherReservationRequest.getDinnerDate()).thenReturn(ANY_HOPPENING_VALID_DATE);
    when(otherReservationRequest.containsRestriction(ALLERGIES_RESTRICTION_NAME)).thenReturn(false);
    when(hoppeningDayService.ingredientExistsForHoppeningDay(ANY_HOPPENING_VALID_DATE, CARROTS))
        .thenReturn(true);
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(otherReservationRequest);

    ReservationRequest aReservationRequest = reservationService.createReservationRequest(reservationRequestDto);

    assertEquals(aReservationRequest, otherReservationRequest);
  }

  @Test
  void givenADateWithinTheFifthFirstDaysMenu_whenCreateReservationNotIncludingTomatoIngredient_thenNewReservationIdIsObtained() {
    otherReservationRequest = includesIngredientBaseExpectation(HOPPENING_FIRST_DAY, false,
        false, false);

    ReservationRequest aReservationRequest = reservationService.createReservationRequest(reservationRequestDto);

    assertEquals(aReservationRequest, otherReservationRequest);
  }

  @Test
  void givenADateWithinTheFifthFirstDaysMenu_whenCreateReservationIncludesTomatoIngredient_thenTooPickyExceptionIsThrown() {
    when(hoppeningDayService.checkIsWithinFiveFirstHoppeningDay(any())).thenReturn(true);
    otherReservationRequest = includesIngredientBaseExpectation(HOPPENING_FIRST_DAY,
        false, true, false);
    when(otherReservationRequest.getRestrictions()).thenReturn(EMPTY_RESTRICTIONS);

    assertThrows(TooPickyException.class, () -> reservationService.createReservationRequest(reservationRequestDto));
  }

  @Test
  void givenADateAfterTheFifthFirstDaysMenu_whenCreateReservationIncludesTomatoIngredient_thenNewReservationIdObtained() {
    otherReservationRequest = includesIngredientBaseExpectation(HOPPENING_NINTH_DAY,
        false, true, false);

    ReservationRequest aReservationRequest = reservationService.createReservationRequest(reservationRequestDto);

    assertEquals(aReservationRequest, otherReservationRequest);
    verify(otherReservationRequest, never()).containsIngredient(TOMATO);
  }

  @Test
  void givenReservationIncludesCarrotsMenuAndAllergiesRestrictions_whenCreateReservation_thenTooPickyExceptionIsThrown() {
    otherReservationRequest = includesIngredientBaseExpectation(HOPPENING_NINTH_DAY, false,
        false, false);
    when(otherReservationRequest.containsRestriction(ALLERGIES_RESTRICTION_NAME)).thenReturn(true);
    when(otherReservationRequest.getRestrictions()).thenReturn(EMPTY_RESTRICTIONS);
    when(otherReservationRequest.containsIngredient(CARROTS)).thenReturn(true);

    assertThrows(TooPickyException.class, () -> reservationService.createReservationRequest(reservationRequestDto));
  }

  @Test
  void whenGettingCustomerCountForPreviousReservedDay_thenSouldDelegateToHoppeningDayService() {
    reservationService.getCustomerCountForPreviousReservedDay(ANY_HOPPENING_VALID_LOCAL_DATE);

    verify(hoppeningDayService).getCustomerCountForPreviousReservedDay(ANY_HOPPENING_VALID_LOCAL_DATE);
  }

  @Test
  void whenCountingRestrictionsForPreviousReservedDay_thenShouldDelegateToHoppeningDayService() {
    reservationService.countRestrictionsForPreviousReservedDay(ANY_HOPPENING_VALID_LOCAL_DATE);

    verify(hoppeningDayService).countRestrictionForPreviousReservedDay(ANY_HOPPENING_VALID_LOCAL_DATE);
  }

  @Test
  void whenGettingCustomerCountForDay_thenSouldDelegateToHoppeningDayService() {
    reservationService.getCustomerCountForDay(ANY_HOPPENING_VALID_LOCAL_DATE);

    verify(hoppeningDayService).getCustomerCountForDate(ANY_HOPPENING_VALID_LOCAL_DATE);
  }

  @Test
  void whenCountingRestrictionsForDay_thenShouldDelegateToHoppeningDayService() {
    reservationService.countRestrictionsForDay(ANY_HOPPENING_VALID_LOCAL_DATE);

    verify(hoppeningDayService).countRestrictionForDate(ANY_HOPPENING_VALID_LOCAL_DATE);
  }

  @Test
  void whenValidatingTimePeriod_thenShouldDelegateToHoppeningDayService() {
    when(hoppeningDayService.getHoppeningDates()).thenReturn(hoppeningDates);
    reservationService.validateTimePeriod(ANY_HOPPENING_VALID_LOCAL_DATE, ANY_HOPPENING_VALID_LOCAL_DATE);

    verify(hoppeningDayService).getHoppeningDates();
  }

  private ReservationRequest includesIngredientBaseExpectation(OffsetDateTime dinnerDate, boolean expectExistingCarrots,
                                                               boolean expectExistingTomato, boolean expectAllergiesPresence) {
    ReservationRequest otherReservationRequest = mock(ReservationRequest.class);
    when(hoppeningDayService.ingredientExistsForHoppeningDay(dinnerDate, CARROTS)).thenReturn(expectExistingCarrots);
    when(hoppeningDayService.restrictionExistsForHoppeningDay(dinnerDate, ALLERGIES_RESTRICTION_NAME))
        .thenReturn(expectAllergiesPresence);
    when(otherReservationRequest.getDinnerDate()).thenReturn(dinnerDate);
    when(otherReservationRequest.containsIngredient(CARROTS)).thenReturn(expectExistingCarrots);
    when(otherReservationRequest.containsIngredient(TOMATO)).thenReturn(expectExistingTomato);
    when(otherReservationRequest.containsRestriction(ALLERGIES_RESTRICTION_NAME)).thenReturn(expectAllergiesPresence);
    when(reservationRequestBuilder.create(reservationRequestDto)).thenReturn(otherReservationRequest);

    return otherReservationRequest;
  }

  private ReservationRequestDto createReservationRequestDto() {
    CountryDto aCountryDto = mock(CountryDto.class);
    String aDate = "2150-05-21T15:23:20.142Z";
    ReservationDetailDto reservationDetailDto = new ReservationDetailDto(aCountryDto, aDate);
    TableDto TABLE_DTO = mock(TableDto.class);
    List<TableDto> tableDtoList = new ArrayList<>();
    tableDtoList.add(TABLE_DTO);
    String aVendorCode = "TEAM";
    String aDinnerDate = "2150-07-21T15:23:20.142Z";
    return new ReservationRequestDto(aVendorCode, aDinnerDate, reservationDetailDto, tableDtoList);
  }
}
