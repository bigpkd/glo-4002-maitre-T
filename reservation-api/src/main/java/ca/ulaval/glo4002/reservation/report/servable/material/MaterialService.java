package ca.ulaval.glo4002.reservation.report.servable.material;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.report.domain.material.Material;
import ca.ulaval.glo4002.reservation.report.domain.material.MaterialNeeds;
import ca.ulaval.glo4002.reservation.report.domain.material.MaterialReportEntry;
import ca.ulaval.glo4002.reservation.report.domain.material.MaterialWasher;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialDayResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialReportResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialReportResponseFactory;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialTotalPriceResponse;
import ca.ulaval.glo4002.reservation.reservation.servable.ReservationService;
import ca.ulaval.glo4002.reservation.utils.Money;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialService {
  private final MaterialWasher materialWasher;
  private final MaterialNeeds materialNeeds;
  private final ReservationService reservationService;
  private final MaterialReportResponseFactory materialReportResponseFactory;
  private final MaterialReportEntryFactory materialReportEntryFactory;
  private final HoppeningDates hoppeningDates;

  @Inject
  public MaterialService(MaterialWasher materialWasher,
                         MaterialNeeds materialNeeds, ReservationService reservationService,
                         MaterialReportResponseFactory materialReportResponseFactory,
                         MaterialReportEntryFactory materialReportEntryFactory,
                         HoppeningDates hoppeningDates) {
    this.materialWasher = materialWasher;
    this.materialNeeds = materialNeeds;
    this.reservationService = reservationService;
    this.materialReportResponseFactory = materialReportResponseFactory;
    this.materialReportEntryFactory = materialReportEntryFactory;
    this.hoppeningDates = hoppeningDates;
  }

  public MaterialReportResponse generateMaterialReportResponseForDays(String startDateString, String endDateString) {

    LocalDate startDate = hoppeningDates.parseDate(startDateString);
    LocalDate endDate = hoppeningDates.parseDate(endDateString);

    reservationService.validateTimePeriod(startDate, endDate);

    int nbCustomerInPreviousDay = reservationService.getCustomerCountForPreviousReservedDay(startDate);
    int nbRestrictionInPreviousDay = reservationService.countRestrictionsForPreviousReservedDay(startDate);
    Map<Material, Integer> availableMaterials =
        materialNeeds.countMaterialNeeds(nbCustomerInPreviousDay, nbRestrictionInPreviousDay);

    List<MaterialReportEntry> materialReportEntries = generateMaterialReportEntryForDays(startDate, endDate,
        availableMaterials);

    return materialReportResponseFactory.create(materialReportEntries);
  }

  private List<MaterialReportEntry> generateMaterialReportEntryForDays(LocalDate startDate, LocalDate endDate,
                                                                       Map<Material, Integer> availableMaterialsPriorToStartDate) {
    List<MaterialReportEntry> materialReportEntries = new ArrayList<>();
    Map<Material, Integer> availableMaterials = new HashMap<>(availableMaterialsPriorToStartDate);

    for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
      int nbCustomerInCurDay = reservationService.getCustomerCountForDay(date);
      int nbRestrictionInCurDay = reservationService.countRestrictionsForDay(date);
      if (nbCustomerInCurDay > 0) {
        Map<Material, Integer> neededMaterials = materialNeeds.countMaterialNeeds(nbCustomerInCurDay, nbRestrictionInCurDay);

        Map<Material, Integer> materialsToBuy = materialNeeds.countMaterialsToBuy(neededMaterials, availableMaterials);
        Map<Material, Integer> materialsToWash = materialWasher.evaluateMaterialsToWash(neededMaterials, availableMaterials);
        Money buyPrice = materialNeeds.computeCostOfMaterials(materialsToBuy);
        Money washPrice = materialWasher.computeWashingCost(materialsToWash);
        Money totalPrice = buyPrice.add(washPrice);

        materialReportEntries.add(materialReportEntryFactory.create(date, materialsToWash, materialsToBuy, totalPrice));
        availableMaterials.putAll(neededMaterials);
      }
    }
    return materialReportEntries;
  }

  public MaterialTotalPriceResponse computeTotalPriceOfMaterialPurchasingAndWashing() {
    String startDate = hoppeningDates.getHoppeningBeginDate().toLocalDate().toString();
    String endDate = hoppeningDates.getHoppeningEndDate().toLocalDate().toString();

    MaterialReportResponse materialReportResponse = generateMaterialReportResponseForDays(startDate, endDate);
    List<MaterialDayResponse> materialReportDates = materialReportResponse.getDates();

    Money totalPriceOfMaterialPurchasingAndWashing = new Money(0.0d);
    for (MaterialDayResponse materialDayResponse : materialReportDates) {
      totalPriceOfMaterialPurchasingAndWashing.add(new Money(materialDayResponse.getTotalPrice()));
    }

    return new MaterialTotalPriceResponse(totalPriceOfMaterialPurchasingAndWashing.getAmount());
  }
}
