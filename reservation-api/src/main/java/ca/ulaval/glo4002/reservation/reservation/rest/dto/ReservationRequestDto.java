package ca.ulaval.glo4002.reservation.reservation.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ReservationRequestDto {
  @NotNull
  public String vendorCode;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ss.SSSZ")
  public String dinnerDate;

  @NotNull
  @Valid
  public ReservationDetailDto from;

  @NotNull
  @Valid
  public List<TableDto> tables;

  public ReservationRequestDto(@JsonProperty(value = "vendorCode") String vendorCode,
                               @JsonProperty(value = "dinnerDate") String dinnerDate,
                               @JsonProperty(value = "from") ReservationDetailDto from,
                               @JsonProperty(value = "tables") List<TableDto> tables) {
    this.vendorCode = vendorCode;
    this.dinnerDate = dinnerDate;
    this.from = from;
    this.tables = tables;
  }
}



