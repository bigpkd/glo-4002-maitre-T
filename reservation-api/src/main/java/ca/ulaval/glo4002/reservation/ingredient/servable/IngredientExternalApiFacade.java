package ca.ulaval.glo4002.reservation.ingredient.servable;

import ca.ulaval.glo4002.reservation.ingredient.rest.dto.IngredientDto;
import ca.ulaval.glo4002.reservation.ingredient.servable.exceptions.IngredientExternalApiFailureException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;

public class IngredientExternalApiFacade implements IngredientApi {
  @Inject
  @Named("externalIngredientApiBasePath")
  String basePath;

  public IngredientExternalApiFacade() {
  }

  @Override
  @GET
  public List<IngredientDto> fetchIngredients() {
    List<IngredientDto> ingredientDtos;
    ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    try {
      URL externalApiUrl = new URL(basePath + "/ingredients");
      ingredientDtos = objectMapper.readValue(externalApiUrl,
          new TypeReference<>() {});
    } catch (Exception e) {
      throw new IngredientExternalApiFailureException(e.getMessage());
    }

    ingredientDtos = rearrangeAndCleanExternalIngredients(ingredientDtos);

    return ingredientDtos;
  }

  private List<IngredientDto> rearrangeAndCleanExternalIngredients(
      List<IngredientDto> externalIngredientsDtos) {
    return externalIngredientsDtos.stream()
        .distinct()
        .collect(Collectors.toList());
  }


}
