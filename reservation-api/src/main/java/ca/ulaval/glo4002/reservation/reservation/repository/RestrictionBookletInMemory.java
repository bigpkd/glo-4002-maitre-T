package ca.ulaval.glo4002.reservation.reservation.repository;

import ca.ulaval.glo4002.reservation.reservation.servable.RestrictionBooklet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RestrictionBookletInMemory implements RestrictionBooklet {
  private final HashMap<String, Double> pricePerRestriction;

  public RestrictionBookletInMemory(Map<String, Double> prices) {
    pricePerRestriction = new HashMap<>(prices);
  }

  @Override
  public Optional<Double> findPriceByName(String name) {
    Optional<Double> price = Optional.empty();
    if (pricePerRestriction.containsKey(name)) {
      price = Optional.of(pricePerRestriction.get(name));
    }
    return price;
  }
}
