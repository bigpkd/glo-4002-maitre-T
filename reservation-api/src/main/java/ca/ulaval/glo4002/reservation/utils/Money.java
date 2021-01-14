package ca.ulaval.glo4002.reservation.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
  private BigDecimal amount;

  public Money(double amount) {
    this.amount = BigDecimal.valueOf(amount);
  }

  public Money(BigDecimal amount) {
    this.amount = amount;
  }

  public Money add(Money secondAmount) {
    return new Money(amount.add(secondAmount.amount));
  }

  public Money multiply(BigDecimal quantity) {
    return new Money(amount.multiply(quantity));
  }

  public boolean equals(Money money) {
    BigDecimal difference = this.amount.subtract(money.amount);
    return ((difference.compareTo(BigDecimal.ZERO) == 0));
  }

  public BigDecimal setRounding(int scale, RoundingMode roundingMode) {
    return amount.setScale(scale, roundingMode);
  }

  public BigDecimal getAmount() {
    return amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Money)) return false;
    Money money = (Money) o;
    return amount.equals(money.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount);
  }
}
