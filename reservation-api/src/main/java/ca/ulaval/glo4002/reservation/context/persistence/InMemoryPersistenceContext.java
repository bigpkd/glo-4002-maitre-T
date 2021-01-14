package ca.ulaval.glo4002.reservation.context.persistence;


import ca.ulaval.glo4002.reservation.context.DefaultEventDates;
import ca.ulaval.glo4002.reservation.context.DefaultReservationValues;
import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDaysRepository;
import ca.ulaval.glo4002.reservation.hoppening.repository.HoppeningDayRepositoryInMemory;
import ca.ulaval.glo4002.reservation.ingredient.domain.IngredientRepository;
import ca.ulaval.glo4002.reservation.ingredient.repository.IngredientRepositoryInMemory;
import ca.ulaval.glo4002.reservation.reservation.domain.ReservationRepository;
import ca.ulaval.glo4002.reservation.reservation.repository.ChefBookletInMemory;
import ca.ulaval.glo4002.reservation.reservation.repository.ReservationRepositoryInMemory;
import javax.inject.Singleton;

import ca.ulaval.glo4002.reservation.reservation.servable.ChefBooklet;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class InMemoryPersistenceContext extends AbstractBinder {
  @Override
  protected void configure() {
    bind(ReservationRepositoryInMemory.class).to(ReservationRepository.class).in(Singleton.class);
    bind(IngredientRepositoryInMemory.class).to(IngredientRepository.class).in(Singleton.class);
    bind(IngredientRepositoryInMemory.class).to(IngredientRepository.class).in(Singleton.class);
    configureHoppeningDayRepositoryInMemory();
  }

  private void configureHoppeningDayRepositoryInMemory() {
    HoppeningDayRepositoryInMemory hoppeningDayRepositoryInMemory =
        createHoppeningDayRepositoryInMemory();
    bind(hoppeningDayRepositoryInMemory).to(HoppeningDaysRepository.class);
  }

  private HoppeningDayRepositoryInMemory createHoppeningDayRepositoryInMemory() {
    ChefBooklet chefBooklet = createChefBooklet();
    return new HoppeningDayRepositoryInMemory(DefaultEventDates.DINNER_DATE_START_DATE.toLocalDate(),
        DefaultEventDates.DINNER_DATE_END_DATE.toLocalDate(), chefBooklet);
  }

  private ChefBooklet createChefBooklet() {
    return new ChefBookletInMemory(DefaultReservationValues.DEFAULT_RESTRICTIONS_PER_CHEF,
        DefaultReservationValues.DEFAULT_MAXIMUM_OF_CUSTOMERS_PER_CHEF,
        DefaultReservationValues.DEFAULT_SALARY_PER_CHEF
        );
  }
}
