package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.Country;
import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationRequest {
  private String vendorCode;
  private OffsetDateTime dinnerDate;
  private OffsetDateTime reservationDate;
  private Country country;
  private List<Table> tables;

  public ReservationRequest(String vendorCode, OffsetDateTime dinnerDate, OffsetDateTime reservationDate,
                            Country country, List<Table> tables) {
    this.vendorCode = vendorCode;
    this.country = country;
    this.tables = tables;
    this.dinnerDate = dinnerDate;
    this.reservationDate = reservationDate;
  }

  public String getVendorCode() {
    return vendorCode;
  }

  public OffsetDateTime getDinnerDate() {
    return dinnerDate;
  }

  public OffsetDateTime getReservationDate() {
    return reservationDate;
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
