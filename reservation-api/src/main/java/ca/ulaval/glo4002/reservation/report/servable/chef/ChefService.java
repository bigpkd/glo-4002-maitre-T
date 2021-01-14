package ca.ulaval.glo4002.reservation.report.servable.chef;

import ca.ulaval.glo4002.reservation.report.domain.chef.ChefReport;
import ca.ulaval.glo4002.reservation.report.domain.chef.ChefReportItem;
import ca.ulaval.glo4002.reservation.reservation.domain.Chef;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ChefService {

  public ChefReport generateChefReport(Map<LocalDate, List<Chef>> assignedChefsPerDate) {
    ChefReport chefReport = new ChefReport();

    for (Map.Entry<LocalDate, List<Chef>> entry : assignedChefsPerDate.entrySet()) {
      List<Chef> assignedChefs = entry.getValue();
      if (!assignedChefs.isEmpty()) {
        LocalDate date = entry.getKey();
        ChefReportItem chefReportItem = new ChefReportItem(date, assignedChefs);
        chefReport.addLine(chefReportItem);
      }
    }

    return chefReport;
  }

}
