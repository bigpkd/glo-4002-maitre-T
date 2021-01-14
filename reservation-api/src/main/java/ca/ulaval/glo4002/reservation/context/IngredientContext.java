package ca.ulaval.glo4002.reservation.context;

import ca.ulaval.glo4002.reservation.context.application.IngredientExternalApiContext;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class IngredientContext extends AbstractBinder {

  @Override
  protected void configure() {
    install(new IngredientExternalApiContext());
  }
}
