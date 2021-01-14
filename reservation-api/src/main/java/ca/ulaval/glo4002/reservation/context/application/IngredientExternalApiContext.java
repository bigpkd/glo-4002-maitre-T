package ca.ulaval.glo4002.reservation.context.application;

import ca.ulaval.glo4002.reservation.ingredient.domain.IngredientRepository;
import ca.ulaval.glo4002.reservation.ingredient.repository.IngredientRepositoryInMemory;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientApi;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientBuilder;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientExternalApiFacade;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class IngredientExternalApiContext extends AbstractBinder {

  @Override
  protected void configure() {

    bindAsContract(IngredientService.class);
    bind(IngredientRepositoryInMemory.class).to(IngredientRepository.class);
    bindAsContract(IngredientRepository.class);
    bind(IngredientExternalApiFacade.class).to(IngredientApi.class);
    bindAsContract(IngredientApi.class);

    String externalIngredientApiBasePath = "http://localhost:8080";
    bind(externalIngredientApiBasePath).to(String.class).named("externalIngredientApiBasePath");
    bindAsContract(IngredientBuilder.class);
  }

}
