package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;

import java.util.List;
import java.util.Optional;

public interface MealBooklet {

  Optional<List<CourseItem>> findCourseItemsNamesByMeal(String mealName);

}
