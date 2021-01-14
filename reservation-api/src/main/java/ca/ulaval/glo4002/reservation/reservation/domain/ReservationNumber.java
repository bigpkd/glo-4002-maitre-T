package ca.ulaval.glo4002.reservation.reservation.domain;

import java.util.Objects;

public class ReservationNumber {
    private final String number;

    private ReservationNumber(String reservationNumberAsString) {
        this.number = reservationNumberAsString;
    }

    public static ReservationNumber create(String vendorCode, long id) {
        return new ReservationNumber(vendorCode.toUpperCase() + "-" + id);
    }

    public static ReservationNumber create(String reservationNumberAsString) {
        return new ReservationNumber(reservationNumberAsString);
    }

    @Override
    public String toString() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationNumber)) return false;
        ReservationNumber that = (ReservationNumber) o;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
