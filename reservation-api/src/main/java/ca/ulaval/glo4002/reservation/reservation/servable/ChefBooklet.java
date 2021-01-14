package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.List;
import java.util.Optional;

public interface ChefBooklet {

  Optional<List<String>> findRestrictionsByChef(String chefName);

  Optional<Integer> findMaximumOfCustomersByChef(String chefName);

  Optional<Money> findSalaryByChef(String chefName);

  List<String> getAllChefNames();
}
