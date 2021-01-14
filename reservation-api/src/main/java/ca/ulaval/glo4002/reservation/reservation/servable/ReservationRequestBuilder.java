package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.reservation.domain.Country;
import ca.ulaval.glo4002.reservation.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidDinnerDateException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidReservationDateException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.ReservationRequestDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.TableDto;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ReservationRequestBuilder {
  private final TableBuilder tableBuilder;
  private final CountryMapper countryMapper;
  private HoppeningDates hoppeningDates;

  @Inject
  public ReservationRequestBuilder(TableBuilder tableBuilder, CountryMapper countryMapper,
                                   HoppeningDates hoppeningDates) {
    this.tableBuilder = tableBuilder;
    this.countryMapper = countryMapper;
    this.hoppeningDates = hoppeningDates;
  }

  public ReservationRequest create(ReservationRequestDto reservationRequestDto) {
    String vendorCode = reservationRequestDto.vendorCode;

    OffsetDateTime dinnerDate = OffsetDateTime.parse(reservationRequestDto.dinnerDate);
    validateDinnerDate(dinnerDate);

    OffsetDateTime reservationDate = OffsetDateTime.parse(reservationRequestDto.from.reservationDate);
    validateReservationDate(reservationDate);

    Country country = countryMapper.toCountry(reservationRequestDto.from.country);
    List<Table> tables = createTables(reservationRequestDto.tables);

    return new ReservationRequest(vendorCode, dinnerDate, reservationDate, country, tables);
  }

  private void validateReservationDate(OffsetDateTime reservationDate) {
    if (!hoppeningDates.validateReservationDate(reservationDate)) {
      throw new InvalidReservationDateException(hoppeningDates.getReservationBeginDate(),
          hoppeningDates.getReservationEndDate());
    }
  }

  private void validateDinnerDate(OffsetDateTime dinnerDate) {
    if (!hoppeningDates.validateDinnerDate(dinnerDate)) {
      throw new InvalidDinnerDateException(hoppeningDates.getHoppeningBeginDate(), hoppeningDates.getHoppeningEndDate());
    }
  }

  public void setHoppeningDates(HoppeningDates hoppeningDates) {
    this.hoppeningDates = hoppeningDates;
  }

  private List<Table> createTables(List<TableDto> tableDtoList) {
    if (tableDtoList.isEmpty()) {
      throw new InvalidReservationQuantityException();
    }

    List<Table> tables = new ArrayList<>();
    for (TableDto tableDto : tableDtoList) {
      tables.add(tableBuilder.create(tableDto));
    }

    return tables;
  }
}