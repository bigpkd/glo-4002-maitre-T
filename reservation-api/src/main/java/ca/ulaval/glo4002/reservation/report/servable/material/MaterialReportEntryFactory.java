package ca.ulaval.glo4002.reservation.report.servable.material;

import ca.ulaval.glo4002.reservation.report.domain.material.Material;
import ca.ulaval.glo4002.reservation.report.domain.material.MaterialReportEntry;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.time.LocalDate;
import java.util.Map;

public class MaterialReportEntryFactory {
  public MaterialReportEntry create(LocalDate date, Map<Material, Integer> materialsToBuy,
                                    Map<Material, Integer> availableMaterials, Money totalCost) {
    return new MaterialReportEntry(date, materialsToBuy, availableMaterials, totalCost);
  }
}
