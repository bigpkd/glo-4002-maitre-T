package ca.ulaval.glo4002.reservation.reservation.domain;

import java.util.ArrayList;
import java.util.List;

public class Meal {
  private final String restrictionName;
  private final List<CourseItem> courseItems;

  public Meal(String restrictionName, List<CourseItem> courseItems) {
    this.restrictionName = restrictionName;
    this.courseItems = new ArrayList<>(courseItems);
  }

  public String getRestrictionName() {
    return restrictionName;
  }

  public List<CourseItem> getCourseItems() {
    return courseItems;
  }

}
