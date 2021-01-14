package ca.ulaval.glo4002.reservation.report.rest.response.material;

import ca.ulaval.glo4002.reservation.report.domain.material.Material;
import ca.ulaval.glo4002.reservation.report.domain.material.MaterialReportEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialReportResponseFactory {

  public MaterialReportResponse create(List<MaterialReportEntry> materialReportEntries) {
    List<MaterialDayResponse> materialDayResponses = new ArrayList<>();

    for(MaterialReportEntry reportEntry : materialReportEntries) {
      Map<Material, Integer> availableMaterials = reportEntry.getAvailableMaterials();
      Map<Material, Integer> materialsToBuy = reportEntry.getMaterialsToBuy();

      Map<String,Integer> cleanedMaterialQuantities = collectMaterialNamesAndQuantities(availableMaterials);
      Map<String,Integer> boughtMaterialQuantities = collectMaterialNamesAndQuantities(materialsToBuy);

      LocalDate date = reportEntry.getDate();
      BigDecimal totalPrice = reportEntry.getTotalCost().getAmount();

      materialDayResponses.add(new MaterialDayResponse(date, cleanedMaterialQuantities, boughtMaterialQuantities
          , totalPrice));
    }

    return new MaterialReportResponse(materialDayResponses);
  }

  private Map<String,Integer> collectMaterialNamesAndQuantities(Map<Material, Integer> materialQuantities) {
    Map<String,Integer> materialNamesAndQuantities = new HashMap<>();

    for(Map.Entry<Material, Integer> entry : materialQuantities.entrySet()){
      materialNamesAndQuantities.put(entry.getKey().getName(), entry.getValue());
    }

    return materialNamesAndQuantities;
  }
}
