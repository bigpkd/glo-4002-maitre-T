package ca.ulaval.glo4002.reservation.report.domain.material;

import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MaterialNeedsTest {
  private static final int A_NUMBER_OF_CUSTOMERS = 8999;
  private static final int A_NUMBER_OF_RESTRICTIONS = 6222;
  private static final int A_QUANTITY = 600;
  private static final int ANOTHER_QUANTITY = 77;
  private static final int A_MATERIAL_TOTAL_QUANTITY = A_NUMBER_OF_CUSTOMERS * A_QUANTITY
      + A_NUMBER_OF_RESTRICTIONS * ANOTHER_QUANTITY;
  private static final int AVAILABLE_QUANTITY = A_QUANTITY;
  private static final double A_COST = 165.0d;
  private static final double ANOTHER_COST = 887.0d;
  private Map<Material, Integer> materialNeedsPerCustomer;
  private Map<Material, Integer> materialNeedsPerRestriction;
  private Map<Material, Integer> totalMaterialNeeds;
  private Map<Material, Integer> materialsToBuy;

  private Material material;
  private Material otherMaterial;
  private MaterialNeeds materialNeeds;

  @BeforeEach
  void setUp() {
    material = mock(Material.class);
    otherMaterial = mock(Material.class);
    setMaterialNeeds();
    materialNeeds = new MaterialNeeds(materialNeedsPerCustomer, materialNeedsPerRestriction);

    when(material.getCost()).thenReturn(new Money(A_COST));
    when(otherMaterial.getCost()).thenReturn(new Money(ANOTHER_COST));
  }

  @Test
  void givenMaterialNeeds_whenCountingMaterialNeeds_materialNeedsForAllCustomersAndAllRestrictionsAreObtained() {
    HashMap<Material, Integer> estimatedMaterialNeeds = materialNeeds.countMaterialNeeds(A_NUMBER_OF_CUSTOMERS,
        A_NUMBER_OF_RESTRICTIONS);

    assertEquals(totalMaterialNeeds, estimatedMaterialNeeds);
  }

  @Test
  void givenMaterialNeeds_whenComputingCostOfMaterials_TotalCostOfAllMaterialsIsObtained() {
    Money estimatedCostOfMaterials = materialNeeds.computeCostOfMaterials(materialsToBuy);

    Money expectedCostOfMaterials = setExpectedCostOfMaterials();
    assertEquals(expectedCostOfMaterials, estimatedCostOfMaterials);
  }

  private Money setExpectedCostOfMaterials() {
    Money expectedCostOfMaterials = (new Money(A_COST)).
        multiply(BigDecimal.valueOf(A_NUMBER_OF_CUSTOMERS * A_QUANTITY - AVAILABLE_QUANTITY)).
        add((new Money(ANOTHER_COST)).multiply(BigDecimal.valueOf(A_MATERIAL_TOTAL_QUANTITY - AVAILABLE_QUANTITY)));
    return expectedCostOfMaterials;
  }

  private void setMaterialNeeds() {
    materialNeedsPerCustomer = Map.ofEntries(
        entry(material, A_QUANTITY),
        entry(otherMaterial, A_QUANTITY)
    );
    materialNeedsPerRestriction = Map.ofEntries(
        entry(material, 0),
        entry(otherMaterial, ANOTHER_QUANTITY)
    );
    totalMaterialNeeds = Map.ofEntries(
        entry(material, A_NUMBER_OF_CUSTOMERS * A_QUANTITY),
        entry(otherMaterial, A_MATERIAL_TOTAL_QUANTITY)
    );
    materialsToBuy = Map.ofEntries(
        entry(material, A_NUMBER_OF_CUSTOMERS * A_QUANTITY - AVAILABLE_QUANTITY),
        entry(otherMaterial, A_MATERIAL_TOTAL_QUANTITY - AVAILABLE_QUANTITY)
    );
  }
}