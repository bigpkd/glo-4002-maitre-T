package ca.ulaval.glo4002.reservation.report.rest.response.ingredient;

import ca.ulaval.glo4002.reservation.report.rest.response.total.AmountResponse;

import java.math.BigDecimal;

public class IngredientTotalPriceResponse extends AmountResponse {
    public IngredientTotalPriceResponse(BigDecimal amount) {
        super(amount);
    }
}
