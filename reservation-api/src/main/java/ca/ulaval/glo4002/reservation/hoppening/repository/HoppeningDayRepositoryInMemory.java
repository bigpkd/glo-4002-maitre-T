package ca.ulaval.glo4002.reservation.hoppening.repository;

import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDay;
import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDaysRepository;
import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportRegister;
import ca.ulaval.glo4002.reservation.reservation.servable.ChefBooklet;
import ca.ulaval.glo4002.reservation.reservation.servable.ChefFactory;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HoppeningDayRepositoryInMemory implements HoppeningDaysRepository {
  private final HashMap<LocalDate, HoppeningDay> hoppeningDays = new HashMap<>();
  private final ChefBooklet chefBooklet;

  public HoppeningDayRepositoryInMemory(LocalDate dinnerStartDate, LocalDate dinnerEndDate, ChefBooklet chefBooklet) {
    this.chefBooklet = chefBooklet;
    configureEventDates(dinnerStartDate, dinnerEndDate);
  }

  public void configureEventDates(LocalDate dinnerStartDate, LocalDate dinnerEndDate) {
    ChefFactory chefFactory = new ChefFactory();
    hoppeningDays.clear();

    for (LocalDate date = dinnerStartDate;
         date.isBefore(dinnerEndDate) || date.isEqual(dinnerEndDate);
         date = date.plusDays(1)) {
      IngredientReportRegister ingredientReportRegister = new IngredientReportRegister();
      hoppeningDays.put(date, new HoppeningDay(date, ingredientReportRegister, chefFactory, chefBooklet));
    }
  }

  public void update(HoppeningDay hoppeningDay) {
    hoppeningDays.replace(hoppeningDay.getDate(), hoppeningDay);
  }

  public HoppeningDay getHoppeningDay(LocalDate date) {
    return hoppeningDays.get(date);
  }


  public List<HoppeningDay> getHoppeningDaysForTimePeriod(LocalDate startDate, LocalDate endDate) {
    List<HoppeningDay> hoppeningDaysForTimePeriod = new ArrayList<>();
    for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate);
         date = date.plusDays(1)) {
      if (hoppeningDays.containsKey(date)) {
        HoppeningDay hoppeningDay = hoppeningDays.get(date);
        hoppeningDaysForTimePeriod.add(hoppeningDay);
      }
    }
    return hoppeningDaysForTimePeriod;
  }

  public HoppeningDay getPreviousDayWithReservation(LocalDate date) {
    LocalDate previousDay = date.minusDays(1);
    HoppeningDay previousReservedHoppeningDay = new HoppeningDay(previousDay, new IngredientReportRegister(), new ChefFactory(), chefBooklet);
    while (hoppeningDays.containsKey(previousDay)) {

      if (hoppeningDays.get(previousDay).hasReservations()) {
        previousReservedHoppeningDay = hoppeningDays.get(previousDay);
        break;
      }
      previousDay = previousDay.minusDays(1);
    }
    return previousReservedHoppeningDay;
  }

  @Override
  public Money computeTotalPriceOfChefs(OffsetDateTime startOffsetDateTime, OffsetDateTime endOffsetDateTime) {
    LocalDate startDate = startOffsetDateTime.toLocalDate();
    LocalDate endDate = endOffsetDateTime.toLocalDate();

    Money totalPriceOfChefs = new Money(0.0d);
    for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
      HoppeningDay hoppeningDay = getHoppeningDay(date);

      if (hoppeningDay != null) {
        Money priceOfChefsOfCurrentDay = hoppeningDay.computeTotalPriceOfChefs();
        totalPriceOfChefs = totalPriceOfChefs.add(priceOfChefsOfCurrentDay);
      }
    }
    return totalPriceOfChefs;
  }
}
