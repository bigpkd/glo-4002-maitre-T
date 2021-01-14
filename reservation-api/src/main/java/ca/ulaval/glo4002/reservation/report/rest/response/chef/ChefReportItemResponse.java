package ca.ulaval.glo4002.reservation.report.rest.response.chef;

import ca.ulaval.glo4002.reservation.report.domain.chef.ChefReportItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ChefReportItemResponse {
  public LocalDate date;
  public List<String> chefs;
  public BigDecimal totalPrice;

  public ChefReportItemResponse(ChefReportItem chefReportItem) {
    this.date = chefReportItem.getDate();
    this.chefs = chefReportItem.getChefs();
    this.totalPrice = chefReportItem.getTotalPrice();
  }
}
