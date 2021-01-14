package ca.ulaval.glo4002.reservation.hoppening.servable;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDay;
import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDaysRepository;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientService;
import ca.ulaval.glo4002.reservation.report.rest.response.ingredient.IngredientReportResponse;
import ca.ulaval.glo4002.reservation.report.servable.ingredient.IngredientReportResponseFactory;
import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidReservationDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HoppeningDayServiceTest {
  private static final OffsetDateTime A_VALID_RESERVATION_DATE = OffsetDateTime.of(2020, 1, 6,
      0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime A_VALID_DINNER_DATE = OffsetDateTime.of(2020, 7, 21,
      0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime A_VALID_RESERVATION_DATE_PLUS_8_DAYS = A_VALID_RESERVATION_DATE.plusDays(8);
  private static final OffsetDateTime A_DATE_BEFORE_HOPPENING = OffsetDateTime.MIN;
  private static final OffsetDateTime A_DATE_AFTER_HOPPENING = OffsetDateTime.MAX;
  private static final LocalDate A_VALID_RESERVATION_LOCAL_DATE = A_VALID_RESERVATION_DATE.toLocalDate();
  private static final LocalDate A_VALID_DINNER_LOCAL_DATE = A_VALID_DINNER_DATE.toLocalDate();
  private static final LocalDate INVALID_DINNER_LOCAL_DATE = A_VALID_DINNER_LOCAL_DATE.minusDays(10);

  private static final Integer A_REMAINING_NUMER_OF_SEATS_FOR_A_VALID_RESERVATION_DATE = 40;
  private static final Integer NB_OF_CUSTOMERS = 5;
  private static final Integer NB_OF_COURSE_ITEMS = 2;
  private static final Integer NB_OF_RESTRICTIONS = 3;

  private static final String REPORT_TYPE_UNIT_STRING = "unit";
  private static final String REPORT_TYPE_TOTAL_STRING = "total";
  private static final String A_VALID_RESERVATION_DATE_PLUS_8_DAYS_LOCAL_DATE_STRING =
      A_VALID_RESERVATION_DATE_PLUS_8_DAYS.toLocalDate().toString();
  private static final String A_VALID_RESERVATION_DATE_LOCAL_DATE_STRING =
      A_VALID_RESERVATION_DATE.toLocalDate().toString();
  private static final String EXISTING_INGREDIENT = "EXISTING_INGREDIENT";
  private static final String RESTRICTION_NAME = "RESTRICTION_NAME";

  private static final IngredientReportResponse A_RESPONSE = mock(IngredientReportResponse.class);

  private Customer customer;
  private CourseItem courseItem;
  private Reservation reservation;
  private HoppeningDaysRepository hoppeningDaysRepository;
  private HoppeningDayService hoppeningDayService;
  private HoppeningDay hoppeningDay;
  private IngredientService ingredientService;
  private IngredientReportResponseFactory ingredientReportResponseFactory;
  private HoppeningDates hoppeningDates;

  @BeforeEach
  void SetUp() {
    hoppeningDaysRepository = mock(HoppeningDaysRepository.class);
    ingredientService = mock(IngredientService.class);
    hoppeningDates = mock(HoppeningDates.class);
    ingredientReportResponseFactory = mock(IngredientReportResponseFactory.class);
    hoppeningDayService =
        new HoppeningDayService(hoppeningDaysRepository, ingredientService, hoppeningDates,
            ingredientReportResponseFactory);
    hoppeningDay = mock(HoppeningDay.class);
    reservation = mock(Reservation.class);
    customer = mock(Customer.class);
    courseItem = mock(CourseItem.class);

    when(reservation.getDinnerDate()).thenReturn(A_VALID_RESERVATION_DATE);
    when(hoppeningDaysRepository.getHoppeningDay(A_VALID_RESERVATION_DATE.toLocalDate())).thenReturn(hoppeningDay);
    when(hoppeningDaysRepository.getHoppeningDay(A_DATE_BEFORE_HOPPENING.toLocalDate()))
        .thenThrow(InvalidReservationDateException.class);
    when(hoppeningDaysRepository.getHoppeningDay(A_DATE_AFTER_HOPPENING.toLocalDate()))
        .thenThrow(InvalidReservationDateException.class);
    when(hoppeningDaysRepository.getHoppeningDay(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);
    when(hoppeningDates.validateDinnerDate(A_VALID_RESERVATION_DATE)).thenReturn(true);
    when(hoppeningDates.validateDinnerDate(A_VALID_RESERVATION_DATE_PLUS_8_DAYS)).thenReturn(true);
    when(hoppeningDates.parseDate(A_VALID_RESERVATION_DATE_LOCAL_DATE_STRING)).thenReturn(A_VALID_RESERVATION_DATE.toLocalDate());
    when(hoppeningDates.parseDate(A_VALID_RESERVATION_DATE_PLUS_8_DAYS_LOCAL_DATE_STRING)).thenReturn(A_VALID_RESERVATION_DATE_PLUS_8_DAYS.toLocalDate());
  }

  @Test
  void givenHoppeningDayService_whenGettingRemainingSeatsOfValidReservationDate_thenRepositoryIsSearchedInto() {
    hoppeningDayService.getCustomerCountForDate(A_VALID_RESERVATION_LOCAL_DATE);

    verify(hoppeningDaysRepository).getHoppeningDay(A_VALID_RESERVATION_LOCAL_DATE);
  }

  @Test
  void givenHoppeningDayService_whenGettingRemainingSeatsOfValidReservationDate_thenRemainingSeatsForThatDateIsObtained() {
    when(hoppeningDay.getCustomerCount())
        .thenReturn(A_REMAINING_NUMER_OF_SEATS_FOR_A_VALID_RESERVATION_DATE);

    int returnedNbOfSeats = hoppeningDayService.getCustomerCountForDate(A_VALID_RESERVATION_LOCAL_DATE);

    assertEquals(A_REMAINING_NUMER_OF_SEATS_FOR_A_VALID_RESERVATION_DATE, returnedNbOfSeats);
  }

  @Test
  void givenHoppeningDayService_whenRegisteringReservation_thenHoppeningDayCorrespondingToDinnerDateIsSearchedForInRepo() {
    hoppeningDayService.registerReservation(reservation);

    verify(hoppeningDaysRepository).getHoppeningDay(A_VALID_RESERVATION_LOCAL_DATE);
  }

  @Test
  void givenHoppeningDayService_whenRegisteringReservation_thenSeatsAreReservedForTheCorrespondingDinnerDate() {
    hoppeningDayService.registerReservation(reservation);

    verify(hoppeningDay).addNbOfCustomers(anyInt());
  }

  @Test
  void givenHoppeningDayService_whenRegisteringReservation_thenCourseItemPricesAreSearchedForEveryClient() {
    ArrayList<Customer> listOfMockedCustomers = createListOfMockedCustomers();
    ArrayList<CourseItem> mockedCourseItems = createListOfMockedCourseItems();
    when(reservation.getCustomers()).thenReturn(listOfMockedCustomers);
    when(customer.getCourseItems()).thenReturn(mockedCourseItems);

    hoppeningDayService.registerReservation(reservation);

    verify(ingredientService, times(NB_OF_CUSTOMERS * NB_OF_COURSE_ITEMS)).
        findPricePerKgForIngredient(any());
  }

  @Test
  void givenHoppeningDayService_whenRegisteringReservation_thenCourseItemsAreRegisteredInIngredientReport() {
    ArrayList<Customer> listOfMockedCustomers = createListOfMockedCustomers();
    when(reservation.getCustomers()).thenReturn(listOfMockedCustomers);

    hoppeningDayService.registerReservation(reservation);

    verify(hoppeningDay, times(NB_OF_CUSTOMERS)).registerIngredientReport(any());
  }

  @Test
  void givenHoppeningDayService_whenRegisteringReservation_thenHoppeningDayIsUpdatedInRepository() {
    ArrayList<Customer> listOfMockedCustomers = createListOfMockedCustomers();
    when(reservation.getCustomers()).thenReturn(listOfMockedCustomers);

    hoppeningDayService.registerReservation(reservation);

    verify(hoppeningDaysRepository).update(hoppeningDay);
  }

  @Test
  void whenGettingIngredientReportForValidTimePeriod_thenHoppeningDaysAreQueriedForTimePeriod() {
    LocalDate expectedStartDate = LocalDate.parse(A_VALID_RESERVATION_DATE_LOCAL_DATE_STRING);
    LocalDate expectedEndDate = LocalDate.parse(A_VALID_RESERVATION_DATE_PLUS_8_DAYS_LOCAL_DATE_STRING);

    hoppeningDayService
        .getIngredientReportForTimePeriod(A_VALID_RESERVATION_DATE_LOCAL_DATE_STRING,
            A_VALID_RESERVATION_DATE_PLUS_8_DAYS_LOCAL_DATE_STRING, REPORT_TYPE_UNIT_STRING);

    verify(hoppeningDaysRepository).getHoppeningDaysForTimePeriod(expectedStartDate, expectedEndDate);
  }

  @Test
  void givenHoppeningDayService_whenGettingIngredientReportForTimePeriodWithTypeUnit_thenResponseIsCreatedWithTypeUnit() {
    hoppeningDayService
        .getIngredientReportForTimePeriod(A_VALID_RESERVATION_DATE_LOCAL_DATE_STRING,
            A_VALID_RESERVATION_DATE_PLUS_8_DAYS_LOCAL_DATE_STRING, REPORT_TYPE_UNIT_STRING);

    verify(ingredientReportResponseFactory).create(any(), eq(REPORT_TYPE_UNIT_STRING));
  }

  @Test
  void givenHoppeningDayService_whenGettingIngredientReportForTimePeriodWithTypeTotal_thenResponseIsCreatedWithTypeTotal() {
    hoppeningDayService
        .getIngredientReportForTimePeriod(A_VALID_RESERVATION_DATE_LOCAL_DATE_STRING,
            A_VALID_RESERVATION_DATE_PLUS_8_DAYS_LOCAL_DATE_STRING,
            REPORT_TYPE_TOTAL_STRING);

    verify(ingredientReportResponseFactory).create(any(), eq(REPORT_TYPE_TOTAL_STRING));
  }

  @Test
  void givenHoppeningDayService_whenGettingIngredientReportForTimePeriod_thenResponseCreatedByResponseFactoryIsObtained() {
    when(ingredientReportResponseFactory.create(any(), any())).thenReturn(A_RESPONSE);

    IngredientReportResponse returnedResponse =
        hoppeningDayService
            .getIngredientReportForTimePeriod(A_VALID_RESERVATION_DATE_LOCAL_DATE_STRING,
                A_VALID_RESERVATION_DATE_PLUS_8_DAYS_LOCAL_DATE_STRING, REPORT_TYPE_UNIT_STRING);

    assertEquals(A_RESPONSE, returnedResponse);
  }

  @Test
  void whenFindingIngredientsForHoppeningDay_thenHoppeningDaysRepositoryShouldGetHoppeningDays() {
    when(hoppeningDaysRepository.getHoppeningDay(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);
    when(hoppeningDay.containsIngredient(EXISTING_INGREDIENT)).thenReturn(true);

    hoppeningDayService.ingredientExistsForHoppeningDay(A_VALID_DINNER_DATE, EXISTING_INGREDIENT);

    verify(hoppeningDaysRepository).getHoppeningDay(A_VALID_DINNER_LOCAL_DATE);
  }

  @Test
  void whenFindingIngredientsForHoppeningDay_thenObtainHoppeningDayWithIngredient() {
    when(hoppeningDaysRepository.getHoppeningDay(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);
    when(hoppeningDay.containsIngredient(EXISTING_INGREDIENT)).thenReturn(true);

    boolean ingredientExists =
        hoppeningDayService.ingredientExistsForHoppeningDay(A_VALID_DINNER_DATE, EXISTING_INGREDIENT);

    assertTrue(ingredientExists);
  }

  @Test
  void whenValidatingRestrictionPresenceForHoppeningDay_thenHoppeningDayObtainedFromHoppeningDaysRepository() {
    when(hoppeningDaysRepository.getHoppeningDay(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);
    when(hoppeningDay.restrictionExistsInDailyRestriction(RESTRICTION_NAME)).thenReturn(false);

    hoppeningDayService.restrictionExistsForHoppeningDay(A_VALID_DINNER_DATE, RESTRICTION_NAME);

    verify(hoppeningDaysRepository).getHoppeningDay(A_VALID_DINNER_LOCAL_DATE);
  }

  @Test
  void whenValidatingRestrictionPresenceForHoppeningDay_thenHoppeningDaysShouldCallContainRestriction() {
    when(hoppeningDaysRepository.getHoppeningDay(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);
    when(hoppeningDay.restrictionExistsInDailyRestriction(RESTRICTION_NAME)).thenReturn(true);

    hoppeningDayService.restrictionExistsForHoppeningDay(A_VALID_DINNER_DATE, RESTRICTION_NAME);

    verify(hoppeningDay).restrictionExistsInDailyRestriction(RESTRICTION_NAME);
  }

  @Test
  void whenValidatingRestrictionPresenceForHoppeningDay_thenRestrictionPresenceObtained() {
    when(hoppeningDaysRepository.getHoppeningDay(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);
    when(hoppeningDay.restrictionExistsInDailyRestriction(RESTRICTION_NAME)).thenReturn(true);

    boolean restrictionExists =
        hoppeningDayService.restrictionExistsForHoppeningDay(A_VALID_DINNER_DATE, RESTRICTION_NAME);

    assertTrue(restrictionExists);
  }

  @Test
  void whenRegisterReservation_thenHoppeningDayShouldAddRestriction() {
    Restriction restriction = mock(Restriction.class);
    List<Restriction> restrictions = Collections.singletonList(restriction);
    when(reservation.getRestrictions()).thenReturn(restrictions);
    when(hoppeningDaysRepository.getHoppeningDay(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);

    hoppeningDayService.registerReservation(reservation);

    verify(hoppeningDay).addRestriction(restrictions);
  }

  @Test
  void whenFetchingPreviousDayWithReservation_thenPreviousHoppeningDayIsObtained() {
    LocalDate dinnerDate = A_VALID_DINNER_LOCAL_DATE;
    LocalDate expectedPreviousDate = dinnerDate.minusDays(1);
    when(hoppeningDay.getDate()).thenReturn(expectedPreviousDate);
    when(hoppeningDaysRepository.getPreviousDayWithReservation(dinnerDate)).thenReturn(hoppeningDay);

    HoppeningDay previousHoppeningDay = hoppeningDayService.fetchPreviousDayWithReservation(dinnerDate);

    assertEquals(expectedPreviousDate, previousHoppeningDay.getDate());
  }

  @Test
  void whenGettingAssignedChefs_thenGetHoppeningBeginDateAndGetHoppeningEndDateAreCalled() {
    when(hoppeningDates.getHoppeningBeginDate()).thenReturn(A_VALID_RESERVATION_DATE);
    when(hoppeningDates.getHoppeningEndDate()).thenReturn(A_VALID_RESERVATION_DATE_PLUS_8_DAYS);

    hoppeningDayService.getAssignedChefs();

    verify(hoppeningDates).getHoppeningBeginDate();
    verify(hoppeningDates).getHoppeningEndDate();
  }

  @Test
  void whenGettingAssignedChefs_thenGetHoppeningDayIsCalled() {
    when(hoppeningDates.getHoppeningBeginDate()).thenReturn(A_VALID_RESERVATION_DATE);
    when(hoppeningDates.getHoppeningEndDate()).thenReturn(A_VALID_RESERVATION_DATE_PLUS_8_DAYS);

    hoppeningDayService.getAssignedChefs();

    int numberOfCalls = (int) DAYS.between(A_VALID_RESERVATION_DATE, A_VALID_RESERVATION_DATE_PLUS_8_DAYS) + 1;
    verify(hoppeningDaysRepository, times(numberOfCalls)).getHoppeningDay(any());
  }

  @Test
  void whenSimulatingAssignedChefWithNewRestriction_thenAssigningChefWithNewRestrictionIsSimulated() {
    Restriction restriction = mock(Restriction.class);
    List<Restriction> restrictions = Collections.singletonList(restriction);

    hoppeningDayService.simulateAssignedChefWithNewRestrictions(A_VALID_RESERVATION_DATE, restrictions);

    verify(hoppeningDay).simulateAssignedChefWithNewRestrictions(restrictions);
  }

  @Test
  void whenGettingCustomerCountForPreviousReservedDay_thenHoppeningDayRepositoryShouldBeCalledToGetPreviousHoppeningDayWithReservation() {
    when(hoppeningDaysRepository.getPreviousDayWithReservation(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);

    hoppeningDayService.getCustomerCountForPreviousReservedDay(A_VALID_DINNER_LOCAL_DATE);

    verify(hoppeningDaysRepository).getPreviousDayWithReservation(A_VALID_DINNER_LOCAL_DATE);
  }

  @Test
  void whenGettingCustomerCountForPreviousReservedDay_thenHoppeningDayShouldBeCalledToCountCustomers() {
    when(hoppeningDaysRepository.getPreviousDayWithReservation(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);

    hoppeningDayService.getCustomerCountForPreviousReservedDay(A_VALID_DINNER_LOCAL_DATE);

    verify(hoppeningDay).getCustomerCount();
  }

  @Test
  void whenGettingCustomerCountForDate_thenHoppeningDayRepositoryShouldBeCalledToGetHoppeningDay() {
    hoppeningDayService.getCustomerCountForDate(A_VALID_DINNER_LOCAL_DATE);

    verify(hoppeningDaysRepository).getHoppeningDay(A_VALID_DINNER_LOCAL_DATE);
  }

  @Test
  void givenDateWithReservations_whenGettingCustomerCountForDate_thenCustomerCountShouldBeObtained() {
    when(hoppeningDay.getCustomerCount()).thenReturn(NB_OF_CUSTOMERS);

    int obtainedCustomerCount = hoppeningDayService.getCustomerCountForDate(A_VALID_DINNER_LOCAL_DATE);

    assertEquals(obtainedCustomerCount, NB_OF_CUSTOMERS);
  }

  @Test
  void givenDateWithoutReservations_whenGettingCustomerCountForDate_thenZeroCustomerShouldBeObtained() {
    when(hoppeningDay.getCustomerCount()).thenReturn(0);

    int count = hoppeningDayService.getCustomerCountForDate(A_VALID_DINNER_LOCAL_DATE);

    assertEquals(0, count);
  }

  @Test
  void givenDateOutsideHoppeningEvent_whenGettingCustomerCountForDate_thenZeroCustomerShouldBeObtained() {
    when(hoppeningDaysRepository.getHoppeningDay(INVALID_DINNER_LOCAL_DATE)).thenReturn(null);

    int count = hoppeningDayService.getCustomerCountForDate(INVALID_DINNER_LOCAL_DATE);

    assertEquals(0, count);
  }

  @Test
  void whenCountingRestrictionForPreviousReservedDay_thenHoppeningDayRepositoryShouldBeCalledToGetPreviousHoppeningDayWithReservation() {
    when(hoppeningDaysRepository.getPreviousDayWithReservation(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);

    hoppeningDayService.countRestrictionForPreviousReservedDay(A_VALID_DINNER_LOCAL_DATE);

    verify(hoppeningDaysRepository).getPreviousDayWithReservation(A_VALID_DINNER_LOCAL_DATE);
  }

  @Test
  void whenCountingRestrictionForPreviousReservedDay_thenHoppeningDayShouldBeCalledToGetPreviousHoppeningDayWithReservation() {
    when(hoppeningDaysRepository.getPreviousDayWithReservation(A_VALID_DINNER_LOCAL_DATE)).thenReturn(hoppeningDay);

    hoppeningDayService.countRestrictionForPreviousReservedDay(A_VALID_DINNER_LOCAL_DATE);

    verify(hoppeningDay).countRestrictionsForMaterialReport();
  }

  @Test
  void whenCountingRestrictionForDate_thenHoppeningDayRepositoryShouldBeCalledToGetHoppeningDay() {
    hoppeningDayService.countRestrictionForDate(A_VALID_DINNER_LOCAL_DATE);

    verify(hoppeningDaysRepository).getHoppeningDay(A_VALID_DINNER_LOCAL_DATE);
  }

  @Test
  void givenDateWithReservations_whenCountingRestrictionForDate_thenRestrictionCountShouldBeObtained() {
    when(hoppeningDay.countRestrictionsForMaterialReport()).thenReturn(NB_OF_RESTRICTIONS);

    int count = hoppeningDayService.countRestrictionForDate(A_VALID_DINNER_LOCAL_DATE);

    assertEquals(NB_OF_RESTRICTIONS, count);
  }

  @Test
  void givenDateWithoutReservations_whenCountingRestrictionForDate_thenZeroRestrictionsShouldBeObtained() {
    when(hoppeningDay.countRestrictionsForMaterialReport()).thenReturn(0);

    int count = hoppeningDayService.countRestrictionForDate(A_VALID_DINNER_LOCAL_DATE);

    assertEquals(0, count);
  }

  @Test
  void givenDateOutsideHoppeningEvent_whenCountingRestrictionForDate_thenZeroRestrictionsShouldBeObtained() {
    when(hoppeningDaysRepository.getHoppeningDay(INVALID_DINNER_LOCAL_DATE)).thenReturn(null);

    int count = hoppeningDayService.countRestrictionForDate(INVALID_DINNER_LOCAL_DATE);

    assertEquals(0, count);
  }

  @Test
  void givenDateWithinFifthFirstDay_whenCheckIsWithinFiveFirstHoppeningDay_thenDateIsWithinFiveFirstDay(){
    when(hoppeningDates.getHoppeningBeginDate()).thenReturn(A_VALID_DINNER_DATE);
    OffsetDateTime aValidDinnerDatePlusLessThanFiveDay = A_VALID_DINNER_DATE.plusDays(3);

    boolean isWithinFiveFirstDay = hoppeningDayService.checkIsWithinFiveFirstHoppeningDay(aValidDinnerDatePlusLessThanFiveDay);

    assertTrue(isWithinFiveFirstDay);
  }

  @Test
  void givenDateNotWithinFifthFirstDay_whenCheckIsWithinFiveFirstHoppeningDay_thenDateIsNotWithinFiveFirstDay(){
    when(hoppeningDates.getHoppeningBeginDate()).thenReturn(A_VALID_DINNER_DATE);
    OffsetDateTime aValidDinnerDatePlusMoreThanFiveDay = A_VALID_DINNER_DATE.plusDays(6);

    boolean isWithinFiveFirstDay = hoppeningDayService.checkIsWithinFiveFirstHoppeningDay(aValidDinnerDatePlusMoreThanFiveDay);

    assertFalse(isWithinFiveFirstDay);
  }

  ArrayList<Customer> createListOfMockedCustomers() {
    ArrayList<Customer> listOfMockedCustomers = new ArrayList<>();
    for (int i = 0; i < NB_OF_CUSTOMERS; i++) {
      listOfMockedCustomers.add(customer);
    }
    return listOfMockedCustomers;
  }

  ArrayList<CourseItem> createListOfMockedCourseItems() {
    ArrayList<CourseItem> listOfMockedCourseItems = new ArrayList<>();
    for (int i = 0; i < HoppeningDayServiceTest.NB_OF_COURSE_ITEMS; i++) {
      listOfMockedCourseItems.add(courseItem);
    }
    return listOfMockedCourseItems;
  }

}