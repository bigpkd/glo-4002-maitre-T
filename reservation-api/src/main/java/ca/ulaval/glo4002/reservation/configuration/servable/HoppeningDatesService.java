package ca.ulaval.glo4002.reservation.configuration.servable;

import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDaysRepository;
import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.configuration.rest.dto.HoppeningDatesDto;
import javax.inject.Inject;

public class HoppeningDatesService {
  private HoppeningDatesFactory hoppeningDatesFactory;
  private HoppeningDates hoppeningDates;
  private HoppeningDaysRepository hoppeningDaysRepository;

  @Inject
  HoppeningDatesService(HoppeningDatesFactory hoppeningDatesFactory, HoppeningDates hoppeningDates,
                        HoppeningDaysRepository hoppeningDaysRepository) {
    this.hoppeningDatesFactory = hoppeningDatesFactory;
    this.hoppeningDates = hoppeningDates;
    this.hoppeningDaysRepository = hoppeningDaysRepository;
  }

  public void changeDates(HoppeningDatesDto hoppeningDatesDto) {
    HoppeningDates newHoppeningDates = hoppeningDatesFactory.create(hoppeningDatesDto);
    hoppeningDates.changeHoppeningDates(newHoppeningDates);
    hoppeningDaysRepository.configureEventDates(hoppeningDates.getHoppeningBeginDate().toLocalDate(),
        hoppeningDates.getHoppeningEndDate().toLocalDate());
  }
}
