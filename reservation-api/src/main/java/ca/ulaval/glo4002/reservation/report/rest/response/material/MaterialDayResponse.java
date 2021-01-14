package ca.ulaval.glo4002.reservation.report.rest.response.material;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MaterialDayResponse {
  private LocalDate date;
  private Map<String,Integer> cleaned;
  private Map<String,Integer> bought;
  private BigDecimal totalPrice;

  public MaterialDayResponse(LocalDate date, Map<String,Integer> cleaned, Map<String,Integer> bought, BigDecimal totalPrice) {
    this.date = date;
    this.cleaned = sortMap(cleaned);
    this.bought = sortMap(bought);
    this.totalPrice = totalPrice;
  }

  private Map<String, Integer> sortMap(Map<String, Integer> map) {
    Map<String, Integer> customizedMap = new LinkedHashMap<>();
    if (!map.isEmpty()) {
      List<String> materialNames = List.of("knife", "fork", "spoon", "bowl", "plate");
      for (String materialName : materialNames) {
        customizedMap.put(materialName, map.get(materialName));
      }
    }
    return customizedMap;
  }

  public LocalDate getDate() {
    return date;
  }

  public Map<String,Integer> getCleaned() {
    return cleaned;
  }

  public Map<String,Integer> getBought() {
    return bought;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }
}
