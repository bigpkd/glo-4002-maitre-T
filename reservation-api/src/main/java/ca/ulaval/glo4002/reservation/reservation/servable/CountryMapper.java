package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.Country;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CountryDto;

public class CountryMapper {
  public Country toCountry(CountryDto countryDto) {
    String code = countryDto.code;
    String fullname = countryDto.fullname;
    String currency = countryDto.currency;
    return new Country(code, fullname, currency);
  }
}
