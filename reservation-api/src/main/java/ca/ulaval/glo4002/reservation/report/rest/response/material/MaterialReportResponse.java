package ca.ulaval.glo4002.reservation.report.rest.response.material;

import java.util.List;

public class MaterialReportResponse {
  private List<MaterialDayResponse> dates;

  public MaterialReportResponse(List<MaterialDayResponse> dates) {
    this.dates = dates;
  }

  public List<MaterialDayResponse> getDates() {
    return dates;
  }
}
