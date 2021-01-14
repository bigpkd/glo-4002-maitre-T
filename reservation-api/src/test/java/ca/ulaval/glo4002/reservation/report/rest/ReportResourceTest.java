package ca.ulaval.glo4002.reservation.report.rest;

import ca.ulaval.glo4002.reservation.hoppening.servable.HoppeningDayService;
import ca.ulaval.glo4002.reservation.report.domain.chef.ChefReport;
import ca.ulaval.glo4002.reservation.report.rest.response.chef.ChefTotalPriceResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.ingredient.IngredientTotalPriceResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialTotalPriceResponse;
import ca.ulaval.glo4002.reservation.report.servable.chef.ChefService;
import ca.ulaval.glo4002.reservation.report.servable.material.MaterialService;
import ca.ulaval.glo4002.reservation.reservation.rest.response.ReservationTotalPriceResponse;
import ca.ulaval.glo4002.reservation.reservation.servable.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReportResourceTest {
  private static final String A_START_DATE_STRING = "2020-07-20";
  private static final String A_END_DATE_STRING = "2020-07-30";
  private static final String A_REPORT_TYPE_STRING = "unit";

  private ReportResource reportResource;
  private HoppeningDayService hoppeningDayService;
  private MaterialService materialService;
  private ReservationService reservationService;
  private ChefService chefService;

  @BeforeEach
  void SetUp() {
    hoppeningDayService = mock(HoppeningDayService.class);
    materialService = mock(MaterialService.class);
    reservationService = mock(ReservationService.class);
    chefService = mock(ChefService.class);
    reportResource = new ReportResource(hoppeningDayService, materialService, reservationService, chefService);
  }

  @Test
  void givenReportResource_whenGettingIngredientReportWithValidQueryParameters_thenResponseTypeOKIsObtained() {
    Response response = reportResource.getIngredientReport(A_START_DATE_STRING, A_END_DATE_STRING,
        A_REPORT_TYPE_STRING);

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
  }

  @Test
  void givenReportResource_whenGettingIngredientReport_thenGetIngredientReportFromHoppeningDayServiceIsCalled(){
    reportResource.getIngredientReport(A_START_DATE_STRING, A_END_DATE_STRING, A_REPORT_TYPE_STRING);

    verify(hoppeningDayService).getIngredientReportForTimePeriod(A_START_DATE_STRING, A_END_DATE_STRING, A_REPORT_TYPE_STRING);
  }

  @Test
  void givenReportResource_whenGettingMaterialReportWithValidQueryParameters_thenResponseTypeOKIsObtained() {
    Response response = reportResource.getMaterialReport(A_START_DATE_STRING, A_END_DATE_STRING);

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
  }

  @Test
  void givenReportResource_whenGettingMaterialReport_thenGetMaterialReportResponseForDaysIsCalled(){
    reportResource.getMaterialReport(A_START_DATE_STRING, A_END_DATE_STRING);

    verify(materialService).generateMaterialReportResponseForDays(A_START_DATE_STRING, A_END_DATE_STRING);
  }

  @Test
  void givenReportResource_whenGettingChefReport_thenResponseTypeOKIsObtained() {
    ChefReport chefReport = mock(ChefReport.class);
    when(chefService.generateChefReport(any())).thenReturn(chefReport);

    Response response = reportResource.getChefReport();

    verify(hoppeningDayService).getAssignedChefs();
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
  }

  @Test
  void givenReportResource_whenGettingTotalReport_thenExpensesAndIncomesAreCollectedFromServices() {
    givenReportResource();

    Response response = reportResource.getTotalReport();

    verify(reservationService).computeTotalPriceOfAllReservations();
    verify(hoppeningDayService).computeTotalPriceOfChefs();
    verify(hoppeningDayService).computeTotalPriceOfIngredients();
    verify(materialService).computeTotalPriceOfMaterialPurchasingAndWashing();
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
  }

  private void givenReportResource() {
    ReservationTotalPriceResponse reservationTotalPriceResponse = mock(ReservationTotalPriceResponse.class);
    ChefTotalPriceResponse chefsTotalPriceResponse = mock(ChefTotalPriceResponse.class);
    IngredientTotalPriceResponse ingredientsTotalPriceResponse = mock(IngredientTotalPriceResponse.class);
    MaterialTotalPriceResponse materialTotalPriceResponse = mock(MaterialTotalPriceResponse.class);

    when(reservationService.computeTotalPriceOfAllReservations()).thenReturn(reservationTotalPriceResponse);
    when(hoppeningDayService.computeTotalPriceOfChefs()).thenReturn(chefsTotalPriceResponse);
    when(hoppeningDayService.computeTotalPriceOfIngredients()).thenReturn(ingredientsTotalPriceResponse);
    when(materialService.computeTotalPriceOfMaterialPurchasingAndWashing()).thenReturn(materialTotalPriceResponse);

    when(reservationTotalPriceResponse.getTotalAmount()).thenReturn(new BigDecimal("0.0"));
    when(chefsTotalPriceResponse.getTotalAmount()).thenReturn(new BigDecimal("0.0"));
    when(ingredientsTotalPriceResponse.getTotalAmount()).thenReturn(new BigDecimal("0.0"));
    when(materialTotalPriceResponse.getTotalAmount()).thenReturn(new BigDecimal("0.0"));
  }
}