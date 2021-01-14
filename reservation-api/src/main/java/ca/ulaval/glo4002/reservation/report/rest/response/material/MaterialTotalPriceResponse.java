package ca.ulaval.glo4002.reservation.report.rest.response.material;

import ca.ulaval.glo4002.reservation.report.rest.response.total.AmountResponse;

import java.math.BigDecimal;

public class MaterialTotalPriceResponse extends AmountResponse {
    public MaterialTotalPriceResponse(BigDecimal amount) {
        super(amount);
    }
}
