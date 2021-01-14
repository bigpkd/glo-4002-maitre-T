package ca.ulaval.glo4002.reservation.context.application;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.configuration.servable.HoppeningDatesFactory;
import ca.ulaval.glo4002.reservation.configuration.servable.HoppeningDatesService;
import ca.ulaval.glo4002.reservation.context.DefaultEventDates;
import ca.ulaval.glo4002.reservation.context.DefaultMaterialValues;
import ca.ulaval.glo4002.reservation.context.DefaultReservationValues;
import ca.ulaval.glo4002.reservation.hoppening.servable.HoppeningDayService;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientService;
import ca.ulaval.glo4002.reservation.report.domain.material.MaterialNeeds;
import ca.ulaval.glo4002.reservation.report.domain.material.MaterialWasher;
import ca.ulaval.glo4002.reservation.report.rest.response.material.MaterialReportResponseFactory;
import ca.ulaval.glo4002.reservation.report.servable.chef.ChefService;
import ca.ulaval.glo4002.reservation.report.servable.ingredient.IngredientReportResponseFactory;
import ca.ulaval.glo4002.reservation.report.servable.material.MaterialFactory;
import ca.ulaval.glo4002.reservation.report.servable.material.MaterialReportEntryFactory;
import ca.ulaval.glo4002.reservation.report.servable.material.MaterialService;
import ca.ulaval.glo4002.reservation.reservation.repository.MealBookletInMemory;
import ca.ulaval.glo4002.reservation.reservation.repository.RestrictionBookletInMemory;
import ca.ulaval.glo4002.reservation.reservation.servable.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationContext extends AbstractBinder {

  @Override
  protected void configure() {
    configureReservation();
    configureHoppeningDayService();
    configureHoppeningSchedule();
    configureHoppeningDates();
    configureMaterialNeedsAndWasher();
    configureMaterialService();

    configureRestrictionBooklet();
    configureMealBooklet();
  }

  private void configureReservation() {
    bindAsContract(ReservationService.class);
    bindAsContract(IngredientService.class);
    bindAsContract(ChefService.class);
    bindAsContract(ReservationRequestBuilder.class);
    bindAsContract(TableBuilder.class);
    bindAsContract(ChefFactory.class);
    bindAsContract(ReservationFactory.class);
    bindAsContract(CustomerFactory.class);
    bindAsContract(RestrictionFactory.class);
    bindAsContract(MealFactory.class);
    bindAsContract(CountryMapper.class);
    bindAsContract(ReservationAssembler.class);
  }

  private void configureHoppeningDayService() {
    bindAsContract(HoppeningDayService.class);
    bindAsContract(IngredientReportResponseFactory.class);
  }

  private void configureHoppeningSchedule() {
    HoppeningDates hoppeningDates = createHoppeningDates();
    bind(hoppeningDates).to(HoppeningDates.class);
  }

  private HoppeningDates createHoppeningDates() {
    return new HoppeningDates(DefaultEventDates.RESERVATION_START_DATE, DefaultEventDates.RESERVATION_END_DATE,
        DefaultEventDates.DINNER_DATE_START_DATE, DefaultEventDates.DINNER_DATE_END_DATE);
  }

  private void configureHoppeningDates() {
    bindAsContract(HoppeningDatesService.class);
    bindAsContract(HoppeningDatesFactory.class);
  }

  private void configureMaterialService() {
    bindAsContract(MaterialService.class);
    bindAsContract(MaterialNeeds.class);
    bindAsContract(MaterialWasher.class);
    bindAsContract(MaterialFactory.class);
    bindAsContract(MaterialReportEntryFactory.class);
    bindAsContract(MaterialReportResponseFactory.class);
  }

  private MaterialNeeds createMaterialNeeds() {
    return new MaterialNeeds(DefaultMaterialValues.MATERIAL_NEEDS_PER_CUSTOMER,
        DefaultMaterialValues.MATERIAL_NEEDS_PER_RESTRICTION);
  }

  private MaterialWasher createMaterialWasher() {
    return new MaterialWasher(DefaultMaterialValues.COST_PER_WASH, DefaultMaterialValues.CAPACITY_PER_WASH);
  }

  private void configureMaterialNeedsAndWasher() {
    MaterialNeeds materialNeeds = createMaterialNeeds();
    bind(materialNeeds).to(MaterialNeeds.class);
    MaterialWasher materialWasher = createMaterialWasher();
    bind(materialWasher).to(MaterialWasher.class);
  }

  private void configureRestrictionBooklet() {
    RestrictionBooklet restrictionBooklet = createRestrictionBooklet();
    bind(restrictionBooklet).to(RestrictionBooklet.class);
  }

  private void configureMealBooklet() {
    MealBooklet mealBooklet = createMealBooklet();
    bind(mealBooklet).to(MealBooklet.class);
  }

  private RestrictionBooklet createRestrictionBooklet() {
    return new RestrictionBookletInMemory(DefaultReservationValues.DEFAULT_PRICE_PER_RESTRICTION);
  }

  private MealBooklet createMealBooklet() {
    return new MealBookletInMemory(DefaultReservationValues.DEFAULT_COURSE_ITEMS_PER_MEAL);
  }
}
