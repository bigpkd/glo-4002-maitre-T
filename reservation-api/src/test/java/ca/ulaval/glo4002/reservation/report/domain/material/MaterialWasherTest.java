package ca.ulaval.glo4002.reservation.report.domain.material;

import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MaterialWasherTest {
  private static final double A_COST_PER_WASH = 111.0d;
  private static final int A_CAPACITY_PER_WASH = 7;
  private static final int A_QUANTITY = 60;
  private static final int ANOTHER_QUANTITY = 7;
  private Map<Material, Integer> materialsToWash;

  private Material material;
  private Material otherMaterial;
  private MaterialWasher materialWasher;

  @BeforeEach
  void setUp() {
    material = mock(Material.class);
    otherMaterial = mock(Material.class);
    materialsToWash = Map.ofEntries(
        entry(material, A_QUANTITY),
        entry(otherMaterial, ANOTHER_QUANTITY)
    );
    materialWasher = new MaterialWasher(new Money(A_COST_PER_WASH), A_CAPACITY_PER_WASH);
  }

  @Test
  void givenMaterialWasher_whenComputingWashingCost_thenTotalWashCostOfAllMaterialsIsObtained() {
    Money estimatedWashingCost = materialWasher.computeWashingCost(materialsToWash);

    Money expectedWashingCost = setExpectedWashingCost();
    assertEquals(expectedWashingCost, estimatedWashingCost);
  }

  private Money setExpectedWashingCost() {
    int totalQuantity = A_QUANTITY + ANOTHER_QUANTITY;
    int washingCount = (totalQuantity % A_CAPACITY_PER_WASH == 0) ? (totalQuantity / A_CAPACITY_PER_WASH) :
        (totalQuantity / A_CAPACITY_PER_WASH + 1);
    return new Money(A_COST_PER_WASH).multiply(BigDecimal.valueOf(washingCount));
  }
}