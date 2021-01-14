package ca.ulaval.glo4002.reservation.report.domain.chef;

import java.util.ArrayList;
import java.util.List;

public class ChefReport {
  private List<ChefReportItem> reportLines;

  public ChefReport() {
    this.reportLines = new ArrayList<>();
  }

  public void addLine(ChefReportItem line) {
    reportLines.add(line);
  }

  public List<ChefReportItem> getLines() {
    return reportLines;
  }
}
