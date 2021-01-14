package ca.ulaval.glo4002.reservation.reservation.repository;

import ca.ulaval.glo4002.reservation.reservation.servable.ChefBooklet;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.*;

public class ChefBookletInMemory implements ChefBooklet {
  private Map<String, List<String>> restrictionsPerChef;
  private Map<String, Integer> MaximumOfCustomersPerChef;
  private Map<String, Money> salaryPerChef;

  public ChefBookletInMemory(Map<String, List<String>> restrictionsPerChef,
                             Map<String, Integer> MaximumOfCustomersPerChef,
                             Map<String, Money> salaryPerChef) {
    this.restrictionsPerChef = new HashMap<>(restrictionsPerChef);
    this.MaximumOfCustomersPerChef = new HashMap<>(MaximumOfCustomersPerChef);
    this.salaryPerChef = new HashMap<>(salaryPerChef);
  }

  @Override
  public Optional<List<String>> findRestrictionsByChef(String chefName) {
    Optional<List<String>> restrictions = Optional.empty();
    if (restrictionsPerChef.containsKey(chefName)) {
      restrictions = Optional.of(restrictionsPerChef.get(chefName));
    }
    return restrictions;
  }

  @Override
  public Optional<Integer> findMaximumOfCustomersByChef(String chefName) {
    Optional<Integer> customerLimit = Optional.empty();
    if (MaximumOfCustomersPerChef.containsKey(chefName)) {
      customerLimit = Optional.of(MaximumOfCustomersPerChef.get(chefName));
    }
    return customerLimit;
  }

  @Override
  public Optional<Money> findSalaryByChef(String chefName) {
    Optional<Money> salary = Optional.empty();
    if (salaryPerChef.containsKey(chefName)) {
      salary = Optional.of(salaryPerChef.get(chefName));
    }
    return salary;
  }

  @Override
  public List<String> getAllChefNames() {
    return new ArrayList<>(restrictionsPerChef.keySet());
  }
}
