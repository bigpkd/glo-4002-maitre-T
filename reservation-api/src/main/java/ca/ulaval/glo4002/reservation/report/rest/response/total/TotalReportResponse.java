package ca.ulaval.glo4002.reservation.report.rest.response.total;

import java.math.BigDecimal;

public class TotalReportResponse {
    private BigDecimal expense;
    private BigDecimal income;
    private BigDecimal profits;

    public TotalReportResponse(TotalIncomeResponse totalIncomeResponse, TotalExpenseResponse totalExpenseResponse) {
        income = totalIncomeResponse.getTotalAmount();
        expense = totalExpenseResponse.getTotalAmount();
        profits = income.subtract(expense);
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public BigDecimal getProfits() {
        return profits;
    }
}
