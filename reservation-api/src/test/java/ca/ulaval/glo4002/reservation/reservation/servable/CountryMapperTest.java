package ca.ulaval.glo4002.reservation.reservation.servable;

import static org.junit.jupiter.api.Assertions.assertEquals;


import ca.ulaval.glo4002.reservation.reservation.domain.Country;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CountryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryMapperTest {
  private CountryMapper countryMapper;
  private CountryDto countryDto;
  public static final String CURRENCY = "CAD";
  public static final String FULLNAME = "Canada";
  public static final String CODE = "CAN";

  @BeforeEach
  void setUp() {
    countryMapper = new CountryMapper();
    countryDto = new CountryDto(CODE, FULLNAME, CURRENCY);
  }

  @Test
  void whenCreatingCountry_thenCodeIsMapped() {
    Country country = countryMapper.toCountry(countryDto);

    assertEquals(country.getCode(), CODE);
  }

  @Test
  void whenCreatingCountry_thenFullNameIsMapped() {
    Country country = countryMapper.toCountry(countryDto);

    assertEquals(country.getFullname(), FULLNAME);
  }

  @Test
  void whenCreatingCountry_thenCurrencyIsMapped() {
    Country country = countryMapper.toCountry(countryDto);

    assertEquals(country.getCurrency(), CURRENCY);
  }

}