package ca.ulaval.glo4002.reservation.hoppening.domain;

import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportRegister;
import ca.ulaval.glo4002.reservation.reservation.domain.Chef;
import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.TooPickyException;
import ca.ulaval.glo4002.reservation.reservation.servable.ChefBooklet;
import ca.ulaval.glo4002.reservation.reservation.servable.ChefFactory;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.time.LocalDate;
import java.util.*;

public class HoppeningDay {
  private final ChefFactory chefFactory;
  private final LocalDate date;
  private final ChefBooklet chefBooklet;
  private final IngredientReportRegister ingredientReportRegister;
  private Map<String, Integer> dailyRestrictions;
  private int customerCount;

  public HoppeningDay(LocalDate date, IngredientReportRegister ingredientReportRegister, ChefFactory chefFactory,
                      ChefBooklet chefBooklet) {
    this.date = date;
    this.chefBooklet = chefBooklet;
    this.customerCount = 0;
    this.chefFactory = chefFactory;
    this.ingredientReportRegister = ingredientReportRegister;
    this.dailyRestrictions = new HashMap<>();
  }

  public void registerIngredientReport(HashMap<CourseItem, Money> pricePerCourseItem) {
    ingredientReportRegister.addToRegister(pricePerCourseItem);
  }

  public int getCustomerCount() {
    return customerCount;
  }

  public void addNbOfCustomers(int nbOfCustomer) {
    customerCount += nbOfCustomer;
  }

  public Money calculateIngredientReportTotal() {
    return ingredientReportRegister.calculateIngredientReportTotal();
  }

  public IngredientReportRegister getIngredientReportRegister() {
    return ingredientReportRegister;
  }

  public LocalDate getDate() {
    return this.date;
  }

  public boolean containsIngredient(String ingredientName) {
    return ingredientReportRegister.containsIngredient(ingredientName);
  }

  public void addRestriction(List<Restriction> restrictions) {
    dailyRestrictions = getUpdatedRestrictionCount(restrictions, dailyRestrictions);
  }

  private Map<String, Integer> getUpdatedRestrictionCount(List<Restriction> restrictions, Map<String, Integer> restrictionCounts) {
    restrictions.forEach((restriction) -> {
      if (restrictionCounts.containsKey(restriction.getName())) {
        restrictionCounts.compute(restriction.getName(), (restrictionName, restrictionCount) -> restrictionCount + 1);
      } else {
        restrictionCounts.put(restriction.getName(), 1);
      }
    });

    return restrictionCounts;
  }

  public boolean hasReservations() {
    return customerCount > 0;
  }

  public boolean restrictionExistsInDailyRestriction(String restrictionName) {
    return dailyRestrictions.containsKey(restrictionName);
  }

  private List<Chef> orderChefsByPriorityForRestriction(String restrictionName, List<Chef> chefs) {
    List<Chef> orderedChefsByPriority = new ArrayList<>();
    int priorityIndex = 0;

    for (Chef chef : chefs) {
      Optional<String> differentRestriction = chef.getDifferentRestriction(restrictionName);
      if (differentRestriction.isPresent() && restrictionExistsInDailyRestriction(differentRestriction.get())) {
        orderedChefsByPriority.add(priorityIndex, chef);
        priorityIndex++;
      } else {
        orderedChefsByPriority.add(chef);
      }
    }

    return orderedChefsByPriority;
  }

  private List<Chef> getChefsAvailableForRestriction(String restrictionName, List<Chef> chefs) {
    List<Chef> possibleChefs = new ArrayList<>();

    for (Chef chef : chefs) {
        if (chef.canCookForRestriction(restrictionName) && chef.canCookForAnAdditionalCustomer()) {
        possibleChefs.add(chef);
      }
    }

    return possibleChefs;
  }

  public List<Chef> getAssignedChefsForDailyRestrictions() {
    return getAssignedChefs(dailyRestrictions);
  }

  public void simulateAssignedChefWithNewRestrictions(List<Restriction> restrictions) {
    Map<String, Integer> simulatedDailyRestriction = getUpdatedRestrictionCount(restrictions, new HashMap<>(dailyRestrictions));
    getAssignedChefs(simulatedDailyRestriction);
  }

  private List<Chef> initializeChefs() {
    List<Chef> chefs = new ArrayList<>();

    for (String chefName : chefBooklet.getAllChefNames()) {
      Optional<List<String>> restrictionsNames = chefBooklet.findRestrictionsByChef(chefName);
      Optional<Integer> maximumOfCustomers = chefBooklet.findMaximumOfCustomersByChef(chefName);
      Optional<Money> salary = chefBooklet.findSalaryByChef(chefName);
      Chef chef = chefFactory.create(chefName, restrictionsNames, maximumOfCustomers, salary);
      chefs.add(chef);
    }

    return chefs;
  }

  public List<Chef> getAssignedChefs(Map<String, Integer> restrictions) {
    ArrayList<Chef> assignedChefs = new ArrayList<>();
    List<Chef> remainingChefs = initializeChefs();
    Map<String, Integer> remainingRestrictions = new HashMap<>(restrictions);

    for (Map.Entry<String, Integer> entry : remainingRestrictions.entrySet()) {
      String restrictionName = entry.getKey();
      int count = entry.getValue();

      for (Chef chef : assignedChefs) {
        if (chef.canCookForRestriction(restrictionName) && chef.canCookForAnAdditionalCustomer()) {
          int availableForChef = chef.getAvailableCustomersCount();
          if (count > availableForChef) {
            chef.setCurrentlyServedCustomers(chef.getMaximumNumberOfCustomersServed());
            count -= availableForChef;
          } else {
            chef.setCurrentlyServedCustomers(chef.getCurrentlyServedCustomers() + count);
            count = 0;
          }
        }
      }

      if (count > 0) {
        List<Chef> possibleChefs = getChefsAvailableForRestriction(restrictionName, remainingChefs);
        List<Chef> orderedChefsByPriority = orderChefsByPriorityForRestriction(restrictionName, possibleChefs);

        for (Chef chef : orderedChefsByPriority) {
          int availableForChef = chef.getAvailableCustomersCount();
          if (count > availableForChef) {
            chef.setCurrentlyServedCustomers(chef.getMaximumNumberOfCustomersServed());
            count -= availableForChef;
          } else {
            chef.setCurrentlyServedCustomers(chef.getCurrentlyServedCustomers() + count);
            count = 0;
          }
          assignedChefs.add(chef);
          if (count == 0) {
            break;
          }
        }
      }

      if (count > 0) {
        throw new TooPickyException();
      }
    }

    return assignedChefs;
  }

  public int countRestrictionsForMaterialReport() {
    int totalCount = 0;
    for (Map.Entry<String, Integer> entry : dailyRestrictions.entrySet()) {
      if (!entry.getKey().equals("no restriction")) {
        totalCount += entry.getValue();
      }
    }
    return totalCount;
  }

  public Money computeTotalPriceOfChefs() {
    Money totalPriceOfChefs = new Money(0.0d);
    List<Chef> assignedChefs = getAssignedChefsForDailyRestrictions();

    if (!assignedChefs.isEmpty()) {
      for (Chef assignedChef : assignedChefs) {
        totalPriceOfChefs = totalPriceOfChefs.add(assignedChef.getSalary());
      }
    }
    return totalPriceOfChefs;
  }

}