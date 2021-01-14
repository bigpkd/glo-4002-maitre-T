package ca.ulaval.glo4002.reservation.report.domain.chef;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChefReportTest {
  private static ChefReport chefReport;
  private static ChefReportItem line;

  @BeforeEach
  void setUp() {
    chefReport = new ChefReport();
    line = mock(ChefReportItem.class);
  }

  @Test
  void givenAChefReport_whenAddingLine_thenLinePresent() {
    chefReport.addLine(line);

    List<ChefReportItem> lines = chefReport.getLines();
    assertTrue(lines.contains(line));
  }
}