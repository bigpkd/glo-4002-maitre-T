package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.reservation.domain.Meal;
import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CustomerDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class CustomerFactory {
  private RestrictionFactory restrictionFactory;
  private MealFactory mealFactory;
  private RestrictionBooklet restrictionBooklet;
  private MealBooklet mealBooklet;
  private static final String NAME_FOR_DUMMY_RESTRICTION = "no restriction" ;

  @Inject
  public CustomerFactory(RestrictionFactory restrictionFactory, MealFactory mealFactory, RestrictionBooklet restrictionBooklet,
                         MealBooklet mealBooklet) {
    this.restrictionFactory = restrictionFactory;
    this.mealFactory = mealFactory;
    this.restrictionBooklet = restrictionBooklet;
    this.mealBooklet = mealBooklet;
  }

  public Customer create(CustomerDto customerDto) {
    String name = customerDto.name;
    List<Restriction> restrictions = createRestrictions(customerDto.restrictions);
    List<Meal> meals = createMeals(restrictions);
    return new Customer(name, restrictions, meals);
  }

  private ArrayList<Restriction> createRestrictions(List<String> restrictionsNames) {
    if (restrictionsNames.isEmpty()) {
      restrictionsNames.add(NAME_FOR_DUMMY_RESTRICTION);
    }

    List<Restriction> restrictionsObjects = restrictionsNames.stream()
        .distinct()
        .sorted()
        .map(restrictionName -> restrictionFactory.create(restrictionName,
            restrictionBooklet.findPriceByName(restrictionName)))
        .collect(Collectors.toList());

    return new ArrayList<>(restrictionsObjects);
  }

  private List<Meal> createMeals(List<Restriction> restrictions) {
    ArrayList<Meal> meals = new ArrayList<>();

    for (Restriction restriction : restrictions) {
      String mealName = restriction.getName();
      Optional<List<CourseItem>> courseItems = mealBooklet.findCourseItemsNamesByMeal(mealName);
      Meal meal = mealFactory.create(mealName, courseItems);
      meals.add(meal);
    }

    return meals;
  }
}
