package ca.ulaval.glo4002.reservation.reservation.domain;

public class Country {
  private String code;
  private String fullname;
  private String currency;

  public Country(String code, String fullname, String currency) {
    this.code = code;
    this.fullname = fullname;
    this.currency = currency;
  }

  public String getCode() {
    return code;
  }

  public String getFullname() {
    return fullname;
  }

  public String getCurrency() {
    return currency;
  }
}
