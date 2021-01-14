package ca.ulaval.glo4002.reservation.reservation.rest.response;

import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class ReservationResponse {
  private static final int ROUNDING_SCALE = 2;
  public BigDecimal reservationPrice;
  public OffsetDateTime dinnerDate;
  public List<CustomerResponse> customers;

  public ReservationResponse(OffsetDateTime dinnerDate, List<Customer> customers, Money reservationPrice) {
    this.dinnerDate = dinnerDate;
    this.customers = customers.stream().map(CustomerResponse::new).collect(Collectors.toList());
    this.reservationPrice = reservationPrice.setRounding(ROUNDING_SCALE, RoundingMode.HALF_UP);
  }
}