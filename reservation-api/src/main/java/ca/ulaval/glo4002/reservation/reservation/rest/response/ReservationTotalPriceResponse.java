package ca.ulaval.glo4002.reservation.reservation.rest.response;

import ca.ulaval.glo4002.reservation.report.rest.response.total.AmountResponse;

import java.math.BigDecimal;

public class ReservationTotalPriceResponse extends AmountResponse {
    public ReservationTotalPriceResponse(BigDecimal amount) {
        super(amount);
    }
}
