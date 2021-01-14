package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.hoppening.servable.HoppeningDayService;
import ca.ulaval.glo4002.reservation.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.reservation.domain.ReservationNumber;
import ca.ulaval.glo4002.reservation.reservation.domain.ReservationRepository;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.TooManyPeopleException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.TooPickyException;
import ca.ulaval.glo4002.reservation.reservation.repository.exceptions.ReservationNotFoundException;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.ReservationRequestDto;
import ca.ulaval.glo4002.reservation.reservation.rest.response.ReservationResponse;
import ca.ulaval.glo4002.reservation.reservation.rest.response.ReservationTotalPriceResponse;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservationService {
  private final ReservationRepository reservationRepository;
  private final ReservationFactory reservationFactory;
  private final HoppeningDayService hoppeningDayService;
  private final ReservationAssembler reservationAssembler;

  private final ReservationRequestBuilder reservationRequestBuilder;
  private static final int MAXIMUM_CUSTOMERS_PER_DAY = 42;
  private static final String ALLERGIES = "allergies";
  private static final String CARROTS = "Carrots";
  private static final String TOMATO = "Tomato";

  @Inject
  public ReservationService(ReservationRepository reservationRepository,
                            ReservationFactory reservationFactory,
                            ReservationAssembler reservationAssembler,
                            HoppeningDayService hoppeningDayService,
                            ReservationRequestBuilder reservationRequestBuilder) {
    this.reservationRepository = reservationRepository;
    this.reservationFactory = reservationFactory;
    this.reservationAssembler = reservationAssembler;
    this.hoppeningDayService = hoppeningDayService;
    this.reservationRequestBuilder = reservationRequestBuilder;
  }

  public String createReservation(ReservationRequestDto reservationRequestDto) {
    ReservationRequest reservationRequest =
        reservationRequestBuilder.create(reservationRequestDto);
    Reservation reservation = reservationFactory.create(reservationRequest);

    reservationRepository.store(reservation);
    hoppeningDayService.registerReservation(reservation);
    return reservationAssembler.toReservationStringNumber(reservation);
  }

  public ReservationResponse findReservation(String reservationNumber) {
    ReservationNumber number = ReservationNumber.create(reservationNumber);
    Reservation reservation = reservationRepository.getReservation(number);
    if (reservation != null) {
      return reservationAssembler.toReservationResponse(reservation);
    } else {
      throw new ReservationNotFoundException(number);
    }
  }

  public ReservationTotalPriceResponse computeTotalPriceOfAllReservations() {
    BigDecimal totalPriceOfAllReservations = reservationRepository.computeTotalPriceOfAllReservations().getAmount();
    return new ReservationTotalPriceResponse(totalPriceOfAllReservations);
  }

  public ReservationRequest createReservationRequest(ReservationRequestDto reservationRequestDto) {
    ReservationRequest reservationRequest = reservationRequestBuilder.create(reservationRequestDto);
    validateNbOfCustomersForRequestedDay(reservationRequest);
    validateForAllergies(reservationRequest);
    validateChefsAvailableForAllCustomers(reservationRequest);

    return reservationRequest;
  }

  public int getCustomerCountForPreviousReservedDay(LocalDate date) {
    return hoppeningDayService.getCustomerCountForPreviousReservedDay(date);
  }

  public int countRestrictionsForPreviousReservedDay(LocalDate date) {
    return hoppeningDayService.countRestrictionForPreviousReservedDay(date);
  }

  public int getCustomerCountForDay(LocalDate date) {
    return hoppeningDayService.getCustomerCountForDate(date);
  }

  public int countRestrictionsForDay(LocalDate date) {
    return hoppeningDayService.countRestrictionForDate(date);
  }

  public void validateTimePeriod(LocalDate startDate, LocalDate endDate) {
    getHoppeningDates().validateTimePeriod(startDate, endDate);
  }

  public HoppeningDates getHoppeningDates() {
    return hoppeningDayService.getHoppeningDates();
  }

  private void validateNbOfCustomersForRequestedDay(ReservationRequest reservationRequest) {
    LocalDate requestedDate = reservationRequest.getDinnerDate().toLocalDate();
    int customersCountInReservationRequest = reservationRequest.countCustomers();
    if (hoppeningDayService.getCustomerCountForDate(requestedDate) + customersCountInReservationRequest
        > MAXIMUM_CUSTOMERS_PER_DAY) {
      throw new TooManyPeopleException();
    }
  }

  private void validateForAllergies(ReservationRequest reservationRequest) {
    if (!checkContainsNoAllergies(reservationRequest)) {
      throw new TooPickyException();
    }
  }

  private void validateChefsAvailableForAllCustomers(ReservationRequest reservationRequest) {
    hoppeningDayService.simulateAssignedChefWithNewRestrictions(reservationRequest.getDinnerDate(),
        reservationRequest.getRestrictions());
  }

  private boolean checkContainsNoAllergies(ReservationRequest reservationRequest) {
    boolean noSimultaniousAllergiesAndCarrotsReserved = areAllergiesAndCarrotsNotSimultaneouslyReserved(reservationRequest);
    boolean noCarrotsWhenAllergiesRestrictionReserved = IsThereNoCarrotsRequestedWhenAllergiesIsAlreadyReserved(reservationRequest);
    boolean noAllergiesOnCarrotsMenuReserved = IsThereNoAllergiesRequestedWhenCarrotsIsAlreadyReserved(reservationRequest);

    boolean noTomatoForFirstFiveDays = true;

    boolean dinnerDateWithinFirstFiveDays =
        hoppeningDayService.checkIsWithinFiveFirstHoppeningDay(reservationRequest.getDinnerDate());

    if (dinnerDateWithinFirstFiveDays) {
      boolean tomatoExist = reservationRequest.containsIngredient(TOMATO);
      if (tomatoExist) {
        noTomatoForFirstFiveDays = false;
      }
    }

    return noSimultaniousAllergiesAndCarrotsReserved && noCarrotsWhenAllergiesRestrictionReserved
        && noAllergiesOnCarrotsMenuReserved && noTomatoForFirstFiveDays;
  }

  private boolean IsThereNoCarrotsRequestedWhenAllergiesIsAlreadyReserved(ReservationRequest reservationRequest) {
    boolean valid = true;
    boolean allergiesReserved =
        hoppeningDayService.restrictionExistsForHoppeningDay(reservationRequest.getDinnerDate(), ALLERGIES);

    if (allergiesReserved) {
      boolean reservationRequestContainsCarrots = reservationRequest.containsIngredient(CARROTS);
      valid = !reservationRequestContainsCarrots;
    }

    return valid;
  }

  private boolean IsThereNoAllergiesRequestedWhenCarrotsIsAlreadyReserved(ReservationRequest reservationRequest) {
    boolean valid = true;
    boolean carrotsReserved =
        hoppeningDayService.ingredientExistsForHoppeningDay(reservationRequest.getDinnerDate(), CARROTS);

    if (carrotsReserved) {
      boolean reservationRequestContainsAllergies = reservationRequest.containsRestriction(ALLERGIES);
      valid = !reservationRequestContainsAllergies;
    }

    return valid;
  }

  private boolean areAllergiesAndCarrotsNotSimultaneouslyReserved(ReservationRequest reservationRequest) {
    boolean allergiesRestrictionRequested = reservationRequest.containsRestriction(ALLERGIES);
    boolean carrotsRequest = reservationRequest.containsIngredient(CARROTS);

    return !(allergiesRestrictionRequested && carrotsRequest);
  }
}
