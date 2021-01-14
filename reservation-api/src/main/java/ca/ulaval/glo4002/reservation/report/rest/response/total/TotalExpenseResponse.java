package ca.ulaval.glo4002.reservation.report.rest.response.total;

import java.util.List;

public class TotalExpenseResponse extends TotalAmountResponse {
    public TotalExpenseResponse(List<AmountResponse> AmountResponses) {
        super(AmountResponses);
    }
}
