package ca.ulaval.glo4002.reservation.report.domain.chef;

import ca.ulaval.glo4002.reservation.reservation.domain.Chef;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ChefReportItem {
  private final LocalDate date;
  private final List<String> chefs;
  private BigDecimal totalPrice;

  public ChefReportItem(LocalDate date, List<Chef> chefs) {
    this.date = date;
    this.chefs = getChefNames(chefs);
    this.totalPrice = calculateTotalPrice(chefs);
  }
  public LocalDate getDate() {
    return date;
  }

  public List<String> getChefs() {
    return chefs;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public BigDecimal calculateTotalPrice(List<Chef> assignedChefs) {
    Money totalPrice = new Money(0);

    for (Chef assignedChef : assignedChefs) {
      totalPrice = totalPrice.add(assignedChef.getSalary());
    }

    return totalPrice.getAmount();
  }

  public List<String> getChefNames(List<Chef> assignedChefs) {
    return assignedChefs.stream()
                          .map(Chef::getName)
                          .sorted(this::compareNoAccents)
                          .collect(Collectors.toList());
  }

  private int compareNoAccents(String firstString, String secondString) {
    firstString = Normalizer.normalize(firstString, Normalizer.Form.NFD);
    secondString = Normalizer.normalize(secondString, Normalizer.Form.NFD);
    return firstString.compareTo(secondString);
  }
}
