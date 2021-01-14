package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.reservation.domain.Meal;
import ca.ulaval.glo4002.reservation.reservation.repository.exceptions.MealNotFoundException;

import java.util.List;
import java.util.Optional;

public class MealFactory {
  public Meal create(String mealName, Optional<List<CourseItem>> courseItems) {
    if (courseItems.isEmpty()) {
      throw new MealNotFoundException(mealName);
    }
    return new Meal(mealName, courseItems.get());
  }
}
