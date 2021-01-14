package ca.ulaval.glo4002.reservation.report.servable.material;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.reservation.servable.ReservationService;
import ca.ulaval.glo4002.reservation.report.domain.material.Material;
import ca.ulaval.glo4002.reservation.report.domain.material.MaterialNeeds;
import ca.ulaval.glo4002.reservation.report.domain.material.MaterialWasher;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialReportResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialReportResponseFactory;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterialServiceTest {
  private static final String START_DATE = "2150-07-21";
  private static final String START_DATE_PLUS_TWO = "2150-07-23";
  private static final String MATERIAL_NAME = "fork";
  private static final Money MATERIAL_UNIT_PRICE = new Money(20);
  private static final Money A_WASHING_COST = new Money(900);
  private static final Money A_BUYING_COST = new Money(500);

  private Material material;
  private ReservationService reservationService;
  private MaterialWasher materialWasher;
  private MaterialNeeds materialNeeds;
  private MaterialReportResponseFactory materialReportResponseFactory;
  private MaterialReportEntryFactory materialReportEntryFactory;
  private MaterialReportResponse materialReportResponse;
  private MaterialService materialService;
  private HoppeningDates hoppeningDates;
  public static final int EXPECTED_NB_OF_DAYS = 3;

  private static final OffsetDateTime RESERVATION_START_DATE =
      OffsetDateTime.of(2030, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime RESERVATION_END_DATE =
      OffsetDateTime.of(2030, 7, 16, 23, 59, 59, 0, ZoneOffset.UTC);
  private static final OffsetDateTime RESERVATION_DINNER_START_DATE =
      OffsetDateTime.of(2030, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime RESERVATION_DINNER_END_DATE =
      OffsetDateTime.of(2030, 7, 30, 23, 59, 59, 0, ZoneOffset.UTC);

  @BeforeEach
  void setUp() {
    materialNeeds = mock(MaterialNeeds.class);
    reservationService = mock(ReservationService.class);
    materialWasher = mock(MaterialWasher.class);
    materialReportResponseFactory = mock(MaterialReportResponseFactory.class);
    materialReportEntryFactory = mock(MaterialReportEntryFactory.class);
    hoppeningDates = new HoppeningDates(RESERVATION_START_DATE, RESERVATION_END_DATE,
        RESERVATION_DINNER_START_DATE, RESERVATION_DINNER_END_DATE);

    material = new Material(MATERIAL_NAME, MATERIAL_UNIT_PRICE);
    materialReportResponse = mock(MaterialReportResponse.class);

    materialService = new MaterialService(materialWasher, materialNeeds, reservationService,
        materialReportResponseFactory, materialReportEntryFactory, hoppeningDates);
  }

  @Test
  void whenGeneratingMaterialReportForDays_thenRestaurantIsCalledToGatherPreviousReservedDayInfoOnlyOnce() {
    generateMaterialReportBaseExpectation();

    materialService.generateMaterialReportResponseForDays(START_DATE, START_DATE_PLUS_TWO);

    verify(reservationService).getCustomerCountForPreviousReservedDay(any());
    verify(reservationService).countRestrictionsForPreviousReservedDay(any());
  }

  @Test
  void whenGeneratingMaterialReportForDays_thenRestaurantIsCalledToGatherDayInfoForEachDayOfTimePeriod() {
    generateMaterialReportBaseExpectation();

    materialService.generateMaterialReportResponseForDays(START_DATE, START_DATE_PLUS_TWO);

    verify(reservationService, times(EXPECTED_NB_OF_DAYS)).getCustomerCountForDay(any());
    verify(reservationService, times(EXPECTED_NB_OF_DAYS)).countRestrictionsForDay(any());
  }

  @Test
  void whenGeneratingMaterialReportForDays_thenMaterialNeedsIsCalledToCountMaterialForEachDayPlusPrevDay() {
    generateMaterialReportBaseExpectation();

    materialService.generateMaterialReportResponseForDays(START_DATE, START_DATE_PLUS_TWO);

    verify(materialNeeds, times(EXPECTED_NB_OF_DAYS + 1)).countMaterialNeeds(anyInt(), anyInt());
  }

  @Test
  void whenGeneratingMaterialReportForDays_thenMaterialNeedsIsCalledToEvaluateBuyingCostForEachDay() {
    generateMaterialReportBaseExpectation();

    materialService.generateMaterialReportResponseForDays(START_DATE, START_DATE_PLUS_TWO);

    verify(materialWasher, times(EXPECTED_NB_OF_DAYS)).computeWashingCost(any());
  }

  @Test
  void whenGeneratingMaterialReportForDays_thenMaterialWasherIsCalledToEvaluateWashingCostForEachDay() {
    generateMaterialReportBaseExpectation();

    materialService.generateMaterialReportResponseForDays(START_DATE, START_DATE_PLUS_TWO);

    verify(materialWasher, times(EXPECTED_NB_OF_DAYS)).computeWashingCost(any());
  }


  @Test
  void whenGeneratingMaterialReportForDays_thenObtainedMaterialReportResponseIsTheOneCreatedByFactory() {
    generateMaterialReportBaseExpectation();

    MaterialReportResponse returnedMaterialReportResponse =
        materialService.generateMaterialReportResponseForDays(START_DATE, START_DATE_PLUS_TWO);
    
    assertEquals(materialReportResponse, returnedMaterialReportResponse);
  }


  private void generateMaterialReportBaseExpectation() {
    int nbCustomerPerDay = 3;
    int nbRestrictionPerDay = 2;
    when(reservationService.getCustomerCountForDay(any())).thenReturn(nbCustomerPerDay);
    when(reservationService.countRestrictionsForDay(any())).thenReturn(nbRestrictionPerDay);
    when(reservationService.getCustomerCountForPreviousReservedDay(any())).thenReturn(nbCustomerPerDay);
    when(reservationService.countRestrictionsForPreviousReservedDay(any())).thenReturn(nbRestrictionPerDay);

    HashMap<Material, Integer> neededMaterial = new HashMap<>();
    int materialQte = 2;
    neededMaterial.put(material, materialQte);

    when(materialNeeds.countMaterialNeeds(nbCustomerPerDay, nbRestrictionPerDay))
        .thenReturn(neededMaterial);
    when(materialNeeds.countMaterialsToBuy(neededMaterial, neededMaterial)).thenReturn(neededMaterial);

    when(materialNeeds.computeCostOfMaterials(neededMaterial)).thenReturn(A_BUYING_COST);

    when(materialWasher.computeWashingCost(any())).thenReturn(A_WASHING_COST);

    when(materialReportResponseFactory.create(any())).thenReturn(materialReportResponse);
  }

}