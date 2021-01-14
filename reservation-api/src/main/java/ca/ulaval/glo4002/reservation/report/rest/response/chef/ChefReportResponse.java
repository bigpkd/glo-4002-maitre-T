package ca.ulaval.glo4002.reservation.report.rest.response.chef;

import ca.ulaval.glo4002.reservation.report.domain.chef.ChefReport;
import ca.ulaval.glo4002.reservation.report.domain.chef.ChefReportItem;

import java.util.List;
import java.util.stream.Collectors;

public class ChefReportResponse {
  public List<ChefReportItemResponse> dates;

  public ChefReportResponse(ChefReport chefReport) {
    this.dates = chefReport.getLines().stream().map(ChefReportItemResponse::new).collect(Collectors.toList());

  }
}
