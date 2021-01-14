package ca.ulaval.glo4002.reservation.reservation.repository;

import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.reservation.servable.MealBooklet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MealBookletInMemory implements MealBooklet {
  private final Map<String, List<CourseItem>> courseItemsPerMeal;

  public MealBookletInMemory(Map<String, List<CourseItem>> courseItemsPerMeal) {
    this.courseItemsPerMeal = new HashMap<>(courseItemsPerMeal);
  }

  @Override
  public Optional<List<CourseItem>> findCourseItemsNamesByMeal(String mealName) {
    Optional<List<CourseItem>> courseItemsNames = Optional.empty();
    if (courseItemsPerMeal.containsKey(mealName)) {
      courseItemsNames = Optional.of(courseItemsPerMeal.get(mealName));
    }
    return courseItemsNames;
  }
}
