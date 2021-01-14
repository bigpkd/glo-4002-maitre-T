package ca.ulaval.glo4002.reservation.report.rest.response.total;

import java.math.BigDecimal;

public class AmountResponse {
    private BigDecimal amount;

    public AmountResponse(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalAmount() {
        return amount;
    }
}
