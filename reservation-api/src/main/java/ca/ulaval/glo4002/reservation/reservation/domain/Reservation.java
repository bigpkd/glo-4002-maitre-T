package ca.ulaval.glo4002.reservation.reservation.domain;

import ca.ulaval.glo4002.reservation.utils.Money;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Reservation {
  private ReservationNumber number;
  private long id;
  private String vendorCode;
  private OffsetDateTime dinnerDate;
  private OffsetDateTime date;
  private Country country;
  private List<Table> tables;
  private Money reservationPrice;

  public Reservation(String vendorCode, OffsetDateTime dinnerDate, OffsetDateTime date, Country country,
                     List<Table> tables) {
    this.vendorCode = vendorCode;
    this.country = country;
    this.tables = tables;
    this.id = this.createUniqueLongId();
    this.dinnerDate = dinnerDate;
    this.date = date;
    this.reservationPrice = this.calculateReservationPrice(tables);
    this.number = ReservationNumber.create(this.vendorCode, this.id);
  }

  private Money calculateReservationPrice(List<Table> tables) {
    Money reservationPrice = new Money(0);

    for (Table table : tables) {
      reservationPrice = reservationPrice.add(table.getTablePrice());
    }

    return reservationPrice;
  }

  public ReservationNumber getNumber() {
    return number;
  }

  public OffsetDateTime getDinnerDate() {
    return dinnerDate;
  }

  public OffsetDateTime getDate() {
    return date;
  }

  public Country getCountry() {
    return country;
  }

  public List<Table> getTables() {
    return tables;
  }

  public List<Customer> getCustomers() {
    ArrayList<Customer> customers = new ArrayList<>();

    for (Table table : tables) {
      customers.addAll(table.getCustomers());
    }

    return customers;
  }

  public List<Restriction> getRestrictions() {
    List<Customer> customers = getCustomers();
    ArrayList<Restriction> restrictions = new ArrayList<>();

    for (Customer customer : customers) {
      List<Restriction> customerRestrictions = customer.getRestrictions();

      restrictions.addAll(customerRestrictions);
    }

    return restrictions;
  }

  private long createUniqueLongId() {
    return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
  }

  public Money getReservationPrice() {
    return reservationPrice;
  }

  public int countCustomers() {
    int count = 0;

    for (Table table : tables) {
      count += table.countCustomers();
    }
    return count;
  }

  public boolean containsRestriction(String restrictionName) {
    for (Table table : tables) {
      if (table.containsRestriction(restrictionName)) {
        return true;
      }
    }

    return false;
  }

  public boolean containsIngredient(String ingredientName) {
    for (Customer customer : getCustomers()) {
      if (customer.hasIngredient(ingredientName)) {
        return true;
      }
    }

    return false;
  }
}
