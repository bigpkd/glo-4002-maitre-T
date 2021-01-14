package ca.ulaval.glo4002.reservation.report.domain.material;

import ca.ulaval.glo4002.reservation.utils.Money;

import java.time.LocalDate;
import java.util.Map;

public class MaterialReportEntry {
  private final LocalDate date;
  private final Map<Material, Integer> availableMaterials;
  private final Map<Material, Integer> materialsToBuy;
  private final Money totalCost;

  public MaterialReportEntry(LocalDate date, Map<Material,Integer> availableMaterials,
                             Map<Material,Integer> materialsToBuy, Money totalCost) {
    this.date = date;
    this.availableMaterials = availableMaterials;
    this.materialsToBuy = materialsToBuy;
    this.totalCost = totalCost;
  }

  public LocalDate getDate() {
    return date;
  }

  public Money getTotalCost() {
    return totalCost;
  }

  public Map<Material, Integer> getAvailableMaterials() {
    return availableMaterials;
  }

  public Map<Material, Integer> getMaterialsToBuy() {
    return materialsToBuy;
  }
}
