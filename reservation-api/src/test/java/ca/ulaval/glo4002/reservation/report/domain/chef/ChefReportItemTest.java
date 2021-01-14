package ca.ulaval.glo4002.reservation.report.domain.chef;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;


import ca.ulaval.glo4002.reservation.report.domain.chef.ChefReportItem;
import ca.ulaval.glo4002.reservation.reservation.domain.Chef;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChefReportItemTest {
  private static ChefReportItem chefReportItem;
  private static final String FIRST_CHEF_NAME = "CHEF 1";
  private static final String SECOND_CHEF_NAME = "CHEF 2";
  private static final Money FIRST_CHEF_SALARY = new Money(500);
  private static final Money SECOND_CHEF_SALARY = new Money(300);
  private static final Money EXPECTED_TOTAL_PRICE = new Money(800);
  private static final int A_NUMBER = 5;
  List<Chef> chefs;

  @BeforeEach
  void setUp() {
    chefs = new ArrayList<>();
    Chef firstChef = new Chef(FIRST_CHEF_NAME, new ArrayList<>(), FIRST_CHEF_SALARY, A_NUMBER);
    Chef secondChef = new Chef(SECOND_CHEF_NAME, new ArrayList<>(), SECOND_CHEF_SALARY, A_NUMBER);
    chefs.add(firstChef);
    chefs.add(secondChef);
    chefReportItem = new ChefReportItem(LocalDate.now(), chefs);
  }

  @Test
  void whenCalculatingTotalPrice_thenTotalSalaryIsReturned() {
    BigDecimal totalPrice = chefReportItem.calculateTotalPrice(chefs);

    assertEquals(totalPrice, EXPECTED_TOTAL_PRICE.getAmount());
  }

  @Test
  void whenGettingChefNames_thenChefNamesAreReturned() {
    List<String> chefNames = chefReportItem.getChefNames(chefs);

    assertEquals(chefNames.get(0), FIRST_CHEF_NAME);
    assertEquals(chefNames.get(1), SECOND_CHEF_NAME);
  }
}