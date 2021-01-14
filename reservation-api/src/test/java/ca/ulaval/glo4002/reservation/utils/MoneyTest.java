package ca.ulaval.glo4002.reservation.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoneyTest {
  private static final double AN_AMOUNT = 25;
  private static final double ANOTHER_AMOUNT = 40;
  private static final double SUM_OF_BOTH_AMOUNTS = 65;
  private static final BigDecimal PRODUCT_OF_BOTH_AMOUNTS = BigDecimal.valueOf(1000);

  private Money anAmount;
  private Money anotherAmount;

  @BeforeEach
  void setUp() {
    anAmount = new Money(AN_AMOUNT);
    anotherAmount = new Money(ANOTHER_AMOUNT);
  }

  @Test
  void givenMoneyAmount_whenComparingToItself_thenAmountsAreEqual() {
    assertTrue(anAmount.equals(anAmount));
  }

  @Test
  void givenTwoDifferentMoneyAmounts_whenComparingOneToAnother_thenAmountsAreNotEqual() {
    assertFalse(anAmount.equals(anotherAmount));
  }

  @Test
  void givenMoneyAmount_whenAddingAnotherMoneyAmount_thenSumOfBothAmountsIsObtained() {
    Money expectedResult = new Money(SUM_OF_BOTH_AMOUNTS);

    Money actualResult = anAmount.add(anotherAmount);

    assertTrue(actualResult.equals(expectedResult));
  }

  @Test
  void givenMoneyAmount_whenMultiplyingToQuantity_thenProductOfAmountAndQuantityIsObtained() {
    Money expectedResult = new Money(PRODUCT_OF_BOTH_AMOUNTS);

    Money actualResult = anAmount.multiply(BigDecimal.valueOf(ANOTHER_AMOUNT));

    assertTrue(actualResult.equals(expectedResult));
  }

  @Test
  void givenMoneyAmount_whenRoundingMoney_thenRoundedAmountWithTheRightScaleIsObtained() {
    BigDecimal initialAmount = BigDecimal.valueOf(45.739292873474018237);
    BigDecimal expectedResult = BigDecimal.valueOf(45.7393);
    int scale = 4;
    RoundingMode roundingMode = RoundingMode.HALF_UP;
    Money amount = new Money(initialAmount);

    BigDecimal result = amount.setRounding(scale, roundingMode);

    assertEquals(expectedResult, result);
  }

  @Test
  void whenGettingMoneyAmount_thenShouldObtainSameAmount() {
    BigDecimal amount = BigDecimal.valueOf(45.739292873474018237);
    Money moneyAmount = new Money(amount);

    BigDecimal result = moneyAmount.getAmount();

    assertEquals(amount, result);
  }
}
