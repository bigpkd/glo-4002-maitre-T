package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.exceptions.InvalidFormatException;
import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.Optional;

public class RestrictionFactory {

  public Restriction create(String restriction, Optional<Double> price) {
    if (price.isEmpty()) {
      throw new InvalidFormatException();
    }
    return new Restriction(restriction, new Money(price.get()));
  }
}
