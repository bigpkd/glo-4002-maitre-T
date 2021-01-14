package ca.ulaval.glo4002.reservation.context;

import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class DefaultReservationValues {
  public static final Map<String, Double> DEFAULT_PRICE_PER_RESTRICTION = Map.ofEntries(
      entry("vegan", 1000.0d),
      entry("vegetarian", 500.0d),
      entry("allergies", 0.0d),
      entry("illness", 0.0d),
      entry("no restriction", 0.0d)
  );

  // CHEF
  public static final int DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS = 5;
  public static final Money DEFAULT_CHEF_SALARY = new Money(6000.00);

  public static final Map<String, List<String>> DEFAULT_RESTRICTIONS_PER_CHEF = Map.ofEntries(
      entry("Thierry Aki", List.of("no restriction")),
      entry("Bob Smarties", List.of("vegan")),
      entry("Bob Rossbeef", List.of("vegetarian")),
      entry("Bill Adicion", List.of("allergies")),
      entry("Omar Calmar", List.of("illness")),
      entry("Amélie Mélo", List.of("vegan", "allergies")),
      entry("Écharlotte Cardin", List.of("vegan", "allergies")),
      entry("Éric Ardo", List.of("vegetarian", "illness")),
      entry("Hans Riz", List.of("no restriction", "illness"))
  );
  public static final Map<String, Integer> DEFAULT_MAXIMUM_OF_CUSTOMERS_PER_CHEF = Map.ofEntries(
      entry("Thierry Aki", DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS),
      entry("Bob Smarties", DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS),
      entry("Bob Rossbeef", DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS),
      entry("Bill Adicion", DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS),
      entry("Omar Calmar", DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS),
      entry("Amélie Mélo", DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS),
      entry("Écharlotte Cardin", DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS),
      entry("Éric Ardo", DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS),
      entry("Hans Riz", DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS)
  );
  public static final Map<String, Money> DEFAULT_SALARY_PER_CHEF = Map.ofEntries(
      entry("Thierry Aki", DEFAULT_CHEF_SALARY),
      entry("Bob Smarties", DEFAULT_CHEF_SALARY),
      entry("Bob Rossbeef", DEFAULT_CHEF_SALARY),
      entry("Bill Adicion", DEFAULT_CHEF_SALARY),
      entry("Omar Calmar", DEFAULT_CHEF_SALARY),
      entry("Amélie Mélo", DEFAULT_CHEF_SALARY),
      entry("Écharlotte Cardin", DEFAULT_CHEF_SALARY),
      entry("Éric Ardo", DEFAULT_CHEF_SALARY),
      entry("Hans Riz", DEFAULT_CHEF_SALARY)
  );

  // MEAL
  public static final Map<String, List<CourseItem>> DEFAULT_COURSE_ITEMS_PER_MEAL = Map.ofEntries(
      entry("no restriction", List.of(new CourseItem("Pork loin", 5),
          new CourseItem("Carrots", 8),
          new CourseItem("Pepperoni", 10),
          new CourseItem("Roast beef", 5),
          new CourseItem("Water", 0.1)
          )
      ),
      entry("vegetarian", List.of(new CourseItem("Pumpkin", 5),
          new CourseItem("Chocolate", 8),
          new CourseItem("Tuna", 10),
          new CourseItem("Mozzarella", 5),
          new CourseItem("Water", 0.1)
          )
      ),
      entry("vegan", List.of(new CourseItem("Tomato", 5),
          new CourseItem("Kiwi", 8),
          new CourseItem("Kimchi", 10),
          new CourseItem("Worcestershire sauce", 5),
          new CourseItem("Water", 0.1)
          )
      ),
      entry("allergies", List.of(new CourseItem("Marmalade", 5),
          new CourseItem("Plantain", 8),
          new CourseItem("Tofu", 10),
          new CourseItem("Bacon", 5),
          new CourseItem("Water", 0.1)
          )
      ),
      entry("illness", List.of(new CourseItem("Scallops", 2),
          new CourseItem("Butternut squash", 4),
          new CourseItem("Kiwi", 5),
          new CourseItem("Pepperoni", 2),
          new CourseItem("Water", 0.1)
          )
      )
  );

}
