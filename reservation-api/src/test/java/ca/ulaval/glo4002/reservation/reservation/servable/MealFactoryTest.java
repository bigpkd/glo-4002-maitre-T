package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.reservation.domain.Meal;
import ca.ulaval.glo4002.reservation.reservation.repository.exceptions.MealNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MealFactoryTest {
  private static final String NO_RESTRICTION = "no restriction";
  private static final String VEGAN_RESTRICTION = "vegan";
  private static final String ALLERGY_RESTRICTION = "allergies";
  private static final String VEGETARIAN_RESTRICTION = "vegetarian";
  private static final String ILLNESS_RESTRICTION = "illness";

  private static final CourseItem NO_RESTRICTION_FIRST_ENTREE = new CourseItem("Pork loin", 5);
  private static final CourseItem NO_RESTRICTION_SECOND_ENTREE = new CourseItem("Carrots", 8);
  private static final CourseItem NO_RESTRICTION_MAIN_DISH = new CourseItem("Pepperoni", 10);
  private static final CourseItem NO_RESTRICTION_DESSERT = new CourseItem("Roast beef", 5);

  private static final CourseItem VEGETARIAN_FIRST_ENTREE = new CourseItem("Pumpkin", 5);
  private static final CourseItem VEGETARIAN_SECOND_ENTREE = new CourseItem("Chocolate", 8);
  private static final CourseItem VEGETARIAN_MAIN_DISH = new CourseItem("Tuna", 10);
  private static final CourseItem VEGETARIAN_DESSERT = new CourseItem("Mozzarella", 5);

  private static final CourseItem VEGAN_FIRST_ENTREE = new CourseItem("Tomato", 5);
  private static final CourseItem VEGAN_SECOND_ENTREE = new CourseItem("Kiwi", 8);
  private static final CourseItem VEGAN_MAIN_DISH = new CourseItem("Kimchi", 10);
  private static final CourseItem VEGAN_DESSERT = new CourseItem("Worcestershire sauce", 5);

  private static final CourseItem ALLERGY_FIRST_ENTREE = new CourseItem("Marmalade", 5);
  private static final CourseItem ALLERGY_SECOND_ENTREE = new CourseItem("Plantain", 8);
  private static final CourseItem ALLERGY_MAIN_DISH = new CourseItem("Tofu", 10);
  private static final CourseItem ALLERGY_DESSERT = new CourseItem("Bacon", 5);

  private static final CourseItem ILLNESS_FIRST_ENTREE = new CourseItem("Scallops", 2);
  private static final CourseItem ILLNESS_SECOND_ENTREE = new CourseItem("Butternut squash", 4);
  private static final CourseItem ILLNESS_MAIN_DISH = new CourseItem("Kiwi", 5);
  private static final CourseItem ILLNESS_DESSERT = new CourseItem("Pepperoni", 2);

  private static final CourseItem ALL_RESTRICTION_DRINK = new CourseItem("Water", 0.1);

  private static final int FIRST_ENTREE_ITEM_POSITION = 0;
  private static final int SECOND_ENTREE_ITEM_POSITION = 1;
  private static final int MAIN_DISH_ITEM_POSITION = 2;
  private static final int DESSERT_ITEM_POSITION = 3;
  private static final int DRINK_ITEM_POSITION = 4;

  private CourseItem firstEntree;
  private CourseItem secondEntree;
  private CourseItem mainDish;
  private CourseItem dessert;
  private CourseItem drink;
  private Meal meal;
  private MealFactory mealFactory;

  @BeforeEach
  void setup() {
    mealFactory = new MealFactory();
  }

  @Test
  void givenNoRestriction_whenCreatingMeal_thenCorrespondingCourseItemAreCreated() {
    meal = mealFactory.create(NO_RESTRICTION, NO_RESTRICTION_COURSE_ITEMS);

    setUpCourseItemForMeal();
    assertEquals(NO_RESTRICTION, meal.getRestrictionName());
    assertEquals(NO_RESTRICTION_FIRST_ENTREE.getIngredientName(), firstEntree.getIngredientName());
    assertEquals(NO_RESTRICTION_FIRST_ENTREE.getWeightInKg(), firstEntree.getWeightInKg());
    assertEquals(NO_RESTRICTION_SECOND_ENTREE.getIngredientName(), secondEntree.getIngredientName());
    assertEquals(NO_RESTRICTION_SECOND_ENTREE.getWeightInKg(), secondEntree.getWeightInKg());
    assertEquals(NO_RESTRICTION_MAIN_DISH.getIngredientName(), mainDish.getIngredientName());
    assertEquals(NO_RESTRICTION_MAIN_DISH.getWeightInKg(), mainDish.getWeightInKg());
    assertEquals(NO_RESTRICTION_DESSERT.getIngredientName(), dessert.getIngredientName());
    assertEquals(NO_RESTRICTION_DESSERT.getWeightInKg(), dessert.getWeightInKg());
    assertEquals(ALL_RESTRICTION_DRINK.getIngredientName(), drink.getIngredientName());
    assertEquals(ALL_RESTRICTION_DRINK.getWeightInKg(), drink.getWeightInKg());
  }

  @Test
  void givenVeganRestriction_whenCreatingMeal_thenCorrespondingCourseItemAreCreated() {
    meal = mealFactory.create(VEGAN_RESTRICTION, VEGAN_COURSE_ITEMS);

    setUpCourseItemForMeal();
    assertEquals(VEGAN_RESTRICTION, meal.getRestrictionName());
    assertEquals(VEGAN_FIRST_ENTREE.getIngredientName(), firstEntree.getIngredientName());
    assertEquals(VEGAN_FIRST_ENTREE.getWeightInKg(), firstEntree.getWeightInKg());
    assertEquals(VEGAN_SECOND_ENTREE.getIngredientName(), secondEntree.getIngredientName());
    assertEquals(VEGAN_SECOND_ENTREE.getWeightInKg(), secondEntree.getWeightInKg());
    assertEquals(VEGAN_MAIN_DISH.getIngredientName(), mainDish.getIngredientName());
    assertEquals(VEGAN_MAIN_DISH.getWeightInKg(), mainDish.getWeightInKg());
    assertEquals(VEGAN_DESSERT.getIngredientName(), dessert.getIngredientName());
    assertEquals(VEGAN_DESSERT.getWeightInKg(), dessert.getWeightInKg());
    assertEquals(ALL_RESTRICTION_DRINK.getIngredientName(), drink.getIngredientName());
    assertEquals(ALL_RESTRICTION_DRINK.getWeightInKg(), drink.getWeightInKg());
  }

  @Test
  void givenVegetarianRestriction_whenCreatingMeal_thenCorrespondingCourseItemAreCreated() {
    meal = mealFactory.create(VEGETARIAN_RESTRICTION, VEGETARIAN_COURSE_ITEMS);

    setUpCourseItemForMeal();
    assertEquals(VEGETARIAN_RESTRICTION, meal.getRestrictionName());
    assertEquals(VEGETARIAN_FIRST_ENTREE.getIngredientName(), firstEntree.getIngredientName());
    assertEquals(VEGETARIAN_FIRST_ENTREE.getWeightInKg(), firstEntree.getWeightInKg());
    assertEquals(VEGETARIAN_SECOND_ENTREE.getIngredientName(), secondEntree.getIngredientName());
    assertEquals(VEGETARIAN_SECOND_ENTREE.getWeightInKg(), secondEntree.getWeightInKg());
    assertEquals(VEGETARIAN_MAIN_DISH.getIngredientName(), mainDish.getIngredientName());
    assertEquals(VEGETARIAN_MAIN_DISH.getWeightInKg(), mainDish.getWeightInKg());
    assertEquals(VEGETARIAN_DESSERT.getIngredientName(), dessert.getIngredientName());
    assertEquals(VEGETARIAN_DESSERT.getWeightInKg(), dessert.getWeightInKg());
    assertEquals(ALL_RESTRICTION_DRINK.getIngredientName(), drink.getIngredientName());
    assertEquals(ALL_RESTRICTION_DRINK.getWeightInKg(), drink.getWeightInKg());
  }

  @Test
  void givenAllergyRestriction_whenCreatingMeal_thenCorrespondingCourseItemAreCreated() {
    meal = mealFactory.create(ALLERGY_RESTRICTION, ALLERGIES_COURSE_ITEMS);

    setUpCourseItemForMeal();
    assertEquals(ALLERGY_RESTRICTION, meal.getRestrictionName());
    assertEquals(ALLERGY_FIRST_ENTREE.getIngredientName(), firstEntree.getIngredientName());
    assertEquals(ALLERGY_FIRST_ENTREE.getWeightInKg(), firstEntree.getWeightInKg());
    assertEquals(ALLERGY_SECOND_ENTREE.getIngredientName(), secondEntree.getIngredientName());
    assertEquals(ALLERGY_SECOND_ENTREE.getWeightInKg(), secondEntree.getWeightInKg());
    assertEquals(ALLERGY_MAIN_DISH.getIngredientName(), mainDish.getIngredientName());
    assertEquals(ALLERGY_MAIN_DISH.getWeightInKg(), mainDish.getWeightInKg());
    assertEquals(ALLERGY_DESSERT.getIngredientName(), dessert.getIngredientName());
    assertEquals(ALLERGY_DESSERT.getWeightInKg(), dessert.getWeightInKg());
    assertEquals(ALL_RESTRICTION_DRINK.getIngredientName(), drink.getIngredientName());
    assertEquals(ALL_RESTRICTION_DRINK.getWeightInKg(), drink.getWeightInKg());
  }

  @Test
  void givenIllnessRestriction_whenCreatingMeal_thenCorrespondingCourseItemAreCreated() {
    meal = mealFactory.create(ILLNESS_RESTRICTION, ILLNESS_COURSE_ITEMS);

    setUpCourseItemForMeal();
    assertEquals(ILLNESS_RESTRICTION, meal.getRestrictionName());
    assertEquals(ILLNESS_FIRST_ENTREE.getIngredientName(), firstEntree.getIngredientName());
    assertEquals(ILLNESS_FIRST_ENTREE.getWeightInKg(), firstEntree.getWeightInKg());
    assertEquals(ILLNESS_SECOND_ENTREE.getIngredientName(), secondEntree.getIngredientName());
    assertEquals(ILLNESS_SECOND_ENTREE.getWeightInKg(), secondEntree.getWeightInKg());
    assertEquals(ILLNESS_MAIN_DISH.getIngredientName(), mainDish.getIngredientName());
    assertEquals(ILLNESS_MAIN_DISH.getWeightInKg(), mainDish.getWeightInKg());
    assertEquals(ILLNESS_DESSERT.getIngredientName(), dessert.getIngredientName());
    assertEquals(ILLNESS_DESSERT.getWeightInKg(), dessert.getWeightInKg());
    assertEquals(ALL_RESTRICTION_DRINK.getIngredientName(), drink.getIngredientName());
    assertEquals(ALL_RESTRICTION_DRINK.getWeightInKg(), drink.getWeightInKg());
  }

  @Test
  void givenNonexistentMeal_whenCreatingMeal_thenMealNotFoundExceptionIsThrown() {
    assertThrows(MealNotFoundException.class, ()->mealFactory.create(VEGAN_RESTRICTION, Optional.empty()));
  }

  void setUpCourseItemForMeal() {
    List<CourseItem> courseItems = meal.getCourseItems();
    firstEntree = courseItems.get(FIRST_ENTREE_ITEM_POSITION);
    secondEntree = courseItems.get(SECOND_ENTREE_ITEM_POSITION);
    mainDish = courseItems.get(MAIN_DISH_ITEM_POSITION);
    dessert = courseItems.get(DESSERT_ITEM_POSITION);
    drink = courseItems.get(DRINK_ITEM_POSITION);
  }

  private static final Optional<List<CourseItem>> NO_RESTRICTION_COURSE_ITEMS = Optional.of(List.of(
      new CourseItem("Pork loin", 5),
      new CourseItem("Carrots", 8),
      new CourseItem("Pepperoni", 10),
      new CourseItem("Roast beef", 5),
      new CourseItem("Water", 0.1)
      )
  );
  private static final Optional<List<CourseItem>> VEGETARIAN_COURSE_ITEMS = Optional.of(List.of(
      new CourseItem("Pumpkin", 5),
      new CourseItem("Chocolate", 8),
      new CourseItem("Tuna", 10),
      new CourseItem("Mozzarella", 5),
      new CourseItem("Water", 0.1)
      )
  );
  private static final Optional<List<CourseItem>> VEGAN_COURSE_ITEMS = Optional.of(List.of(
      new CourseItem("Tomato", 5),
      new CourseItem("Kiwi", 8),
      new CourseItem("Kimchi", 10),
      new CourseItem("Worcestershire sauce", 5),
      new CourseItem("Water", 0.1)
      )
  );
  private static final Optional<List<CourseItem>> ALLERGIES_COURSE_ITEMS = Optional.of(List.of(
      new CourseItem("Marmalade", 5),
      new CourseItem("Plantain", 8),
      new CourseItem("Tofu", 10),
      new CourseItem("Bacon", 5),
      new CourseItem("Water", 0.1)
      )
  );
  private static final Optional<List<CourseItem>> ILLNESS_COURSE_ITEMS = Optional.of(List.of(
      new CourseItem("Scallops", 2),
      new CourseItem("Butternut squash", 4),
      new CourseItem("Kiwi", 5),
      new CourseItem("Pepperoni", 2),
      new CourseItem("Water", 0.1)
      )
  );
}