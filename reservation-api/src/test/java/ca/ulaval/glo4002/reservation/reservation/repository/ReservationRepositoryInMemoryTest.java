package ca.ulaval.glo4002.reservation.reservation.repository;

import ca.ulaval.glo4002.reservation.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.reservation.domain.ReservationNumber;
import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReservationRepositoryInMemoryTest {
  private static final ReservationNumber NUMBER = ReservationNumber.create("VENDOR", 1L);
  private static final ReservationNumber SECOND_NUMBER = ReservationNumber.create("code", 2L);

  private static final int EMPTY_RESERVATION_TOTAL = 0;
  private static final int ONE_EXISTING_RESERVATION_TOTAL = 1;

  private static final String EXISTING_RESTRICTION = "EXISTING_RESTRICTION";
  private static final String INEXISTING_RESTRICTION = "INEXISTING_RESTRICTION";

  private static final Money A_RESERVATION_PRICE = new Money(1511.23d);
  private static final Money ANOTHER_RESERVATION_PRICE = new Money(2311.15d);

  private ReservationRepositoryInMemory reservationRepositoryInMemory;
  private Reservation reservation;
  private Reservation second_reservation;

  @BeforeEach
  void setup() {
    reservationRepositoryInMemory = new ReservationRepositoryInMemory();
    reservation = mock(Reservation.class);
    second_reservation = mock(Reservation.class);
  }

  @Test
  void givenReservationRepository_whenGettingNumberOfReservations_thenEmptyReservationTotalIsObtained() {
    assertEquals(EMPTY_RESERVATION_TOTAL, reservationRepositoryInMemory.getNumberOfReservations());
  }

  @Test
  void givenReservationRepository_whenStoringReservation_thenReservationIsStoredInRepository() {
    reservationRepositoryInMemory.store(reservation);

    assertEquals(ONE_EXISTING_RESERVATION_TOTAL,
        reservationRepositoryInMemory.getNumberOfReservations());
  }

  @Test
  void givenNonEmptyReservationRepository_whenGettingReservationWithNumber_thenCorrespondingReservationIsObtained() {
    when(reservation.getNumber()).thenReturn(NUMBER);

    reservationRepositoryInMemory.store(reservation);

    assertEquals(reservation, reservationRepositoryInMemory.getReservation(NUMBER));
  }

  @Test
  void whenSearchingForAContainedRestriction_thenShouldRestrictionFound() {
    when(reservation.getNumber()).thenReturn(NUMBER);
    when(second_reservation.getNumber()).thenReturn(SECOND_NUMBER);
    when(reservation.containsRestriction(EXISTING_RESTRICTION)).thenReturn(true);
    when(second_reservation.containsRestriction(EXISTING_RESTRICTION)).thenReturn(false);
    reservationRepositoryInMemory.store(reservation);
    reservationRepositoryInMemory.store(second_reservation);

    boolean restrictionFound = reservationRepositoryInMemory.containsRestriction(EXISTING_RESTRICTION);

    assertTrue(restrictionFound);
  }

  @Test
  void whenSearchingForAnInexistingRestriction_thenShouldNotFoundRestriction() {
    when(reservation.getNumber()).thenReturn(NUMBER);
    when(second_reservation.getNumber()).thenReturn(SECOND_NUMBER);
    when(reservation.containsRestriction(EXISTING_RESTRICTION)).thenReturn(true);
    when(second_reservation.containsRestriction(EXISTING_RESTRICTION)).thenReturn(false);
    reservationRepositoryInMemory.store(reservation);
    reservationRepositoryInMemory.store(second_reservation);

    boolean restrictionFound = reservationRepositoryInMemory.containsRestriction(INEXISTING_RESTRICTION);

    assertFalse(restrictionFound);
  }

  @Test
  void givenReservationRepository_whenComputingTotalPriceOfAllReservations_thenTotalPriceOfAllReservationsIsObtained() {
    when(reservation.getNumber()).thenReturn(NUMBER);
    when(second_reservation.getNumber()).thenReturn(SECOND_NUMBER);
    when(reservation.getReservationPrice()).thenReturn(A_RESERVATION_PRICE);
    when(second_reservation.getReservationPrice()).thenReturn(ANOTHER_RESERVATION_PRICE);
    reservationRepositoryInMemory.store(reservation);
    reservationRepositoryInMemory.store(second_reservation);

    Money computedTotalPrice = reservationRepositoryInMemory.computeTotalPriceOfAllReservations();

    Money expectedTotalPrice = A_RESERVATION_PRICE.add(ANOTHER_RESERVATION_PRICE);
    assertTrue(expectedTotalPrice.equals(computedTotalPrice));
  }
}