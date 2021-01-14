package ca.ulaval.glo4002.reservation.hoppening.servable;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDay;
import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDaysRepository;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientService;
import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportItem;
import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportRegister;
import ca.ulaval.glo4002.reservation.report.rest.response.chef.ChefTotalPriceResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.ingredient.IngredientReportResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.ingredient.IngredientTotalPriceResponse;
import ca.ulaval.glo4002.reservation.report.servable.ingredient.IngredientReportResponseFactory;
import ca.ulaval.glo4002.reservation.reservation.domain.*;
import ca.ulaval.glo4002.reservation.utils.Money;

import javax.inject.Inject;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class  HoppeningDayService {
  private final IngredientReportResponseFactory ingredientReportResponseFactory;
  private final HoppeningDaysRepository hoppeningDaysRepository;
  private final IngredientService ingredientService;
  private final HoppeningDates hoppeningDates;
  private static final int ROUNDING_SCALE = 2;

  @Inject
  public HoppeningDayService(HoppeningDaysRepository hoppeningDaysRepository,
                             IngredientService ingredientService,
                             HoppeningDates hoppeningDates,
                             IngredientReportResponseFactory ingredientReportResponseFactory) {
    this.hoppeningDaysRepository = hoppeningDaysRepository;
    this.ingredientService = ingredientService;
    this.hoppeningDates = hoppeningDates;
    this.ingredientReportResponseFactory = ingredientReportResponseFactory;
  }

  public void registerReservation(Reservation reservation) {
    HoppeningDay hoppeningDay = getHoppeningDay(reservation.getDinnerDate());
    hoppeningDay.addRestriction(reservation.getRestrictions());
    hoppeningDay.addNbOfCustomers(reservation.countCustomers());

    for (Customer customer : reservation.getCustomers()) {
      List<CourseItem> courseItems = customer.getCourseItems();
      HashMap<CourseItem, Money> pricePerCourseItems = findPricePerKgForEveryCourseItem(courseItems);
      hoppeningDay.registerIngredientReport(pricePerCourseItems);
    }

    hoppeningDaysRepository.update(hoppeningDay);
  }

  private HashMap<CourseItem, Money> findPricePerKgForEveryCourseItem(List<CourseItem> courseItems) {
    HashMap<CourseItem, Money> pricePerCourseItems = new HashMap<>();

    for (CourseItem courseItem : courseItems) {
      String ingredientName = courseItem.getIngredientName();
      Money ingredientPricePerKg = ingredientService.findPricePerKgForIngredient(ingredientName);
      pricePerCourseItems.put(courseItem, ingredientPricePerKg);
    }

    return pricePerCourseItems;
  }

  public void simulateAssignedChefWithNewRestrictions(OffsetDateTime dinnerDate, List<Restriction> restrictions) {
    HoppeningDay hoppeningDay = getHoppeningDay(dinnerDate);
    hoppeningDay.simulateAssignedChefWithNewRestrictions(restrictions);
  }

  public Map<LocalDate, List<Chef>> getAssignedChefs() {
    LocalDate startDate = hoppeningDates.getHoppeningBeginDate().toLocalDate();
    LocalDate endDate = hoppeningDates.getHoppeningEndDate().toLocalDate();
    Map<LocalDate, List<Chef>> assignedChefsPerDate = new HashMap<>();

    for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
      HoppeningDay hoppeningDay = hoppeningDaysRepository.getHoppeningDay(date);

      if (hoppeningDay != null) {
        List<Chef> assignedChefs = hoppeningDay.getAssignedChefsForDailyRestrictions();
        assignedChefsPerDate.put(date, assignedChefs);
      }
    }

    return assignedChefsPerDate;
  }

  public IngredientReportResponse getIngredientReportForTimePeriod(String startDateString, String endDateString,
                                                                   String reportTypeString) {
    List<HoppeningDay> hoppeningDaysForTimePeriod = fetchHoppeningDaysForTimePeriod(startDateString, endDateString);

    return ingredientReportResponseFactory.create(hoppeningDaysForTimePeriod, reportTypeString);
  }

  public boolean ingredientExistsForHoppeningDay(OffsetDateTime dinnerDate, String ingredientName) {
    HoppeningDay hoppeningDay = getHoppeningDay(dinnerDate);
    return hoppeningDay != null && hoppeningDay.containsIngredient(ingredientName);
  }

  private List<HoppeningDay> fetchHoppeningDaysForTimePeriod(String startDateString, String endDateString) {
    LocalDate startDate = hoppeningDates.parseDate(startDateString);
    LocalDate endDate = hoppeningDates.parseDate(endDateString);

    hoppeningDates.validateTimePeriod(startDate, endDate);

    return hoppeningDaysRepository.getHoppeningDaysForTimePeriod(startDate, endDate);
  }

  public HoppeningDay fetchPreviousDayWithReservation(LocalDate dinnerDate) {
    return hoppeningDaysRepository.getPreviousDayWithReservation(dinnerDate);
  }

  public boolean restrictionExistsForHoppeningDay(OffsetDateTime dinnerDate, String restrictionName) {
    HoppeningDay hoppeningDay = hoppeningDaysRepository.getHoppeningDay(dinnerDate.toLocalDate());
    if (hoppeningDay != null) {
      return hoppeningDay.restrictionExistsInDailyRestriction(restrictionName);
    }
    return false;
  }

  private HoppeningDay getHoppeningDay(OffsetDateTime dinnerDate) {
    return hoppeningDaysRepository.getHoppeningDay(dinnerDate.toLocalDate());
  }

  public int countRestrictionForPreviousReservedDay(LocalDate date) {
    HoppeningDay prevHoppeningDay = hoppeningDaysRepository.getPreviousDayWithReservation(date);
    return prevHoppeningDay.countRestrictionsForMaterialReport();
  }

  public int getCustomerCountForPreviousReservedDay(LocalDate date) {
    HoppeningDay prevHoppeningDay = hoppeningDaysRepository.getPreviousDayWithReservation(date);
    return prevHoppeningDay.getCustomerCount();
  }

  public int getCustomerCountForDate(LocalDate date) {
    HoppeningDay hoppeningDay = hoppeningDaysRepository.getHoppeningDay(date);
    return (hoppeningDay == null) ? 0 : hoppeningDay.getCustomerCount();
  }

  public int countRestrictionForDate(LocalDate date) {
    HoppeningDay hoppeningDay = hoppeningDaysRepository.getHoppeningDay(date);
    return (hoppeningDay == null) ? 0 : hoppeningDay.countRestrictionsForMaterialReport();
  }

  public HoppeningDates getHoppeningDates() {
    return hoppeningDates;
  }

  public boolean checkIsWithinFiveFirstHoppeningDay(OffsetDateTime dinnerDate) {
    OffsetDateTime fifthFirstDay = hoppeningDates.getHoppeningBeginDate().plusDays(5);
    return dinnerDate.isBefore(fifthFirstDay);
  }

  public ChefTotalPriceResponse computeTotalPriceOfChefs() {
    Money totalPriceOfChefs = hoppeningDaysRepository.computeTotalPriceOfChefs(hoppeningDates.getHoppeningBeginDate(),
            hoppeningDates.getHoppeningEndDate());
    return new ChefTotalPriceResponse(totalPriceOfChefs.getAmount());
  }

  public IngredientTotalPriceResponse computeTotalPriceOfIngredients() {
    String startDateString = hoppeningDates.getHoppeningBeginDate().toLocalDate().toString();
    String endDateString = hoppeningDates.getHoppeningEndDate().toLocalDate().toString();
    List<HoppeningDay> hoppeningDaysForTimePeriod = fetchHoppeningDaysForTimePeriod(startDateString, endDateString);

    Money totalPriceForAllDays = new Money(0.0d);
    for (HoppeningDay hoppeningDay : hoppeningDaysForTimePeriod) {

      IngredientReportRegister ingredientReportRegister = hoppeningDay.getIngredientReportRegister();
      Collection<IngredientReportItem> temporaryIngredientReportItems =
              ingredientReportRegister.getIngredientReportItems().values()
                      .stream()
                      .map(e -> (IngredientReportItem) e.clone())
                      .collect(Collectors.toList());

      for (IngredientReportItem ingredientReportItem : temporaryIngredientReportItems) {
        totalPriceForAllDays = totalPriceForAllDays.add(ingredientReportItem.getTotalPrice());
      }
    }
    totalPriceForAllDays = new Money(totalPriceForAllDays.setRounding(ROUNDING_SCALE, RoundingMode.HALF_UP).doubleValue());
    return new IngredientTotalPriceResponse(totalPriceForAllDays.getAmount());
  }
}