package ca.ulaval.glo4002.reservation.report.rest;

import ca.ulaval.glo4002.reservation.report.rest.response.chef.ChefTotalPriceResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.ingredient.IngredientTotalPriceResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialReportResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialTotalPriceResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.total.AmountResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.total.TotalExpenseResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.total.TotalIncomeResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.total.TotalReportResponse;
import ca.ulaval.glo4002.reservation.report.servable.chef.ChefService;
import ca.ulaval.glo4002.reservation.report.servable.material.MaterialService;
import ca.ulaval.glo4002.reservation.report.domain.chef.ChefReport;
import ca.ulaval.glo4002.reservation.report.rest.response.chef.ChefReportResponse;
import ca.ulaval.glo4002.reservation.hoppening.servable.HoppeningDayService;
import ca.ulaval.glo4002.reservation.report.rest.response.ingredient.IngredientReportResponse;
import ca.ulaval.glo4002.reservation.reservation.domain.Chef;
import ca.ulaval.glo4002.reservation.reservation.rest.response.ReservationTotalPriceResponse;
import ca.ulaval.glo4002.reservation.reservation.servable.ReservationService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportResource {
  private final HoppeningDayService hoppeningDayService;
  private final MaterialService materialService;
  private final ReservationService reservationService;
  private final ChefService chefService;

  @Inject
  public ReportResource(HoppeningDayService hoppeningDayService, MaterialService materialService, ReservationService reservationService, ChefService chefService) {
    this.hoppeningDayService = hoppeningDayService;
    this.materialService = materialService;
    this.reservationService = reservationService;
    this.chefService = chefService;
  }

  @GET
  @Path("/ingredients")
  public Response getIngredientReport(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate,
                                      @QueryParam("type") String reportType) {
    IngredientReportResponse response = hoppeningDayService.getIngredientReportForTimePeriod(startDate, endDate, reportType);

    return Response.status(Response.Status.OK).entity(response).build();
  }

  @GET
  @Path("/chefs")
  public Response getChefReport() {
    Map<LocalDate, List<Chef>> assignedChefs = hoppeningDayService.getAssignedChefs();
    ChefReport report = chefService.generateChefReport(assignedChefs);

    ChefReportResponse response = new ChefReportResponse(report);

    return Response.status(Response.Status.OK).entity(response).build();
  }

  @GET
  @Path("/material")
  public Response getMaterialReport(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
    MaterialReportResponse response = materialService.generateMaterialReportResponseForDays(startDate, endDate);

    return Response.status(Response.Status.OK).entity(response).build();
  }

  @GET
  @Path("/total")
  public Response getTotalReport() {
    ReservationTotalPriceResponse reservationTotalPriceResponse = reservationService.computeTotalPriceOfAllReservations();
    ChefTotalPriceResponse chefsTotalPriceResponse = hoppeningDayService.computeTotalPriceOfChefs();
    IngredientTotalPriceResponse ingredientsTotalPriceResponse = hoppeningDayService.computeTotalPriceOfIngredients();
    MaterialTotalPriceResponse materialTotalPriceResponse = materialService.computeTotalPriceOfMaterialPurchasingAndWashing();

    List<AmountResponse> income = Collections.singletonList(reservationTotalPriceResponse);
    TotalIncomeResponse totalIncomeResponse = new TotalIncomeResponse(income);
    List<AmountResponse> expenses = Arrays.asList(chefsTotalPriceResponse, ingredientsTotalPriceResponse, materialTotalPriceResponse);
    TotalExpenseResponse totalExpenseResponse = new TotalExpenseResponse(expenses);

    TotalReportResponse response = new TotalReportResponse(totalIncomeResponse, totalExpenseResponse);

    return Response.status(Response.Status.OK).entity(response).build();
  }

}
