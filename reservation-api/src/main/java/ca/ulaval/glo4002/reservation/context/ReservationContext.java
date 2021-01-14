package ca.ulaval.glo4002.reservation.context;

import ca.ulaval.glo4002.reservation.context.application.ApplicationContext;
import ca.ulaval.glo4002.reservation.context.persistence.InMemoryPersistenceContext;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ReservationContext extends AbstractBinder {
  @Override
  protected void configure() {
    install(new ApplicationContext());
    install(new InMemoryPersistenceContext());
  }
}
