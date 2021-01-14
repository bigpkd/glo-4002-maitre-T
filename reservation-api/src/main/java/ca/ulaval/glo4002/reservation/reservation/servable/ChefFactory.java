package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.Chef;
import ca.ulaval.glo4002.reservation.reservation.repository.exceptions.ChefNotFoundException;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChefFactory {

  public Chef create(String name,
                     Optional<List<String>> restrictionNames,
                     Optional<Integer> maximumOfCustomers,
                     Optional<Money> salary) {
    if (restrictionNames.isEmpty() || maximumOfCustomers.isEmpty() || salary.isEmpty()) {
      throw new ChefNotFoundException(name);
    }

    return new Chef(name,
        new ArrayList<>(restrictionNames.get()),
        salary.get(),
        maximumOfCustomers.get());
  }

}
