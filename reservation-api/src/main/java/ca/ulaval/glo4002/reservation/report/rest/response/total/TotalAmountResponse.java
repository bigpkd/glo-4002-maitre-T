package ca.ulaval.glo4002.reservation.report.rest.response.total;

import java.math.BigDecimal;
import java.util.List;

public class TotalAmountResponse {
    private List<AmountResponse> AmountResponses;

    public TotalAmountResponse(List<AmountResponse> AmountResponses) {
        this.AmountResponses = AmountResponses;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal totalAmount = new BigDecimal(0.0d);
        for (AmountResponse AmountResponse : AmountResponses) {
            totalAmount = totalAmount.add(AmountResponse.getTotalAmount());
        }
        return totalAmount;
    }
}
