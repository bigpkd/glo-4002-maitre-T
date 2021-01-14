package ca.ulaval.glo4002.reservation.report.rest.response.total;

import java.util.List;

public class TotalIncomeResponse extends TotalAmountResponse {
    public TotalIncomeResponse(List<AmountResponse> AmountResponses) {
        super(AmountResponses);
    }
}
