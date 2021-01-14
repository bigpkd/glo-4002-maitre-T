package ca.ulaval.glo4002.reservation.report.rest.response.chef;

import ca.ulaval.glo4002.reservation.report.rest.response.total.AmountResponse;

import java.math.BigDecimal;

public class ChefTotalPriceResponse extends AmountResponse {
    public ChefTotalPriceResponse(BigDecimal amount) {
        super(amount);
    }
}
