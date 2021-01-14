package ca.ulaval.glo4002.reservation.reservation.domain;

import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Customer {
  private static final Money BASE_CUSTOMER_PRICE = new Money(1000);

  private String name;
  private List<Restriction> restrictions;
  private List<Meal> meals;
  private Money customerPrice;

  public Customer(String name, List<Restriction> restrictions, List<Meal> meals) {
    this.name = name;
    this.restrictions = restrictions;
    this.meals = meals;
    this.customerPrice = this.calculateCustomerPrice();
  }

  private Money calculateCustomerPrice() {
    customerPrice = new Money(0);
    customerPrice = customerPrice.add(BASE_CUSTOMER_PRICE);

    for (Restriction restriction : restrictions) {
      customerPrice = customerPrice.add(restriction.getPrice());
    }

    return customerPrice;
  }

  public List<Restriction> getRestrictions() {
    return restrictions;
  }

  public List<String> getRestrictionNames() {
    return restrictions.stream()
        .filter(r -> !r.isAnActualRestriction())
        .map(Restriction::getName)
        .collect(Collectors.toList());
  }

  public Money getCustomerPrice() {
    return customerPrice;
  }

  public String getName() {
    return name;
  }

  public boolean hasRestriction(String restrictionName) {
    for (Restriction restriction : restrictions) {
      if (restriction.getName().equals(restrictionName)) {
        return true;
      }
    }

    return false;
  }

  public List<CourseItem> getCourseItems() {
    ArrayList<CourseItem> courseItems = new ArrayList<>();

    for (Meal meal : meals) {
      courseItems.addAll(meal.getCourseItems());
    }

    return courseItems;
  }

  public boolean hasIngredient(String ingredientName) {
    for (CourseItem courseItem : getCourseItems()) {
      if (courseItem.getIngredientName().equals(ingredientName)) {
        return true;
      }
    }

    return false;
  }
}
