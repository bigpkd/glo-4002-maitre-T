package ca.ulaval.glo4002.reservation.reservation.servable;

import java.util.Optional;

public interface RestrictionBooklet {

  Optional<Double> findPriceByName(String name);
}
