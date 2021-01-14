package ca.ulaval.glo4002.reservation.report.domain.material;

import ca.ulaval.glo4002.reservation.utils.Money;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MaterialWasher {
  private final Money costPerWash;
  private final int capacityPerWash;

  public MaterialWasher(Money costPerWash, int capacityPerWash) {
    this.costPerWash = costPerWash;
    this.capacityPerWash = capacityPerWash;
  }

  public Money computeWashingCost(Map<Material,Integer> materialsToWash) {
    Integer totalCountOfMaterials = 0;
    for (Integer countOfMaterial : materialsToWash.values()) {
      totalCountOfMaterials += countOfMaterial;
    }

    int countOfWashings =  (int) Math.ceil(Double.valueOf(totalCountOfMaterials)/Double.valueOf(capacityPerWash));
    Money washingCost = costPerWash.multiply(BigDecimal.valueOf(countOfWashings));

    return washingCost;
  }

  public Map<Material, Integer> evaluateMaterialsToWash(Map<Material, Integer> neededMaterials,
                                                        Map<Material, Integer> availableMaterials) {
    Map<Material, Integer> materialsToWash = new HashMap<>(availableMaterials);
    if (isAvailableMaterialsEnoughToCoverNeeds(neededMaterials, availableMaterials)) {
      materialsToWash.putAll(neededMaterials);
    }
    return materialsToWash;
  }

  private boolean isAvailableMaterialsEnoughToCoverNeeds(Map<Material, Integer> neededMaterials,
                                                         Map<Material, Integer> availableMaterials) {
    boolean isAvailableMaterialsEnough = true;
    if (availableMaterials.isEmpty()) {
      isAvailableMaterialsEnough = false;
    } else {
      for (Map.Entry<Material, Integer> entry : neededMaterials.entrySet()) {
        boolean isNeededMaterialAvailable = availableMaterials.containsKey(entry.getKey());
        boolean isAvailableQuantityLessThanNeededQuantity = availableMaterials.get(entry.getKey()) < entry.getValue();
        boolean isAvailableMaterialsNotEnough = isNeededMaterialAvailable && isAvailableQuantityLessThanNeededQuantity;
        if (isAvailableMaterialsNotEnough) {
          isAvailableMaterialsEnough = false;
        }
      }
    }
    return isAvailableMaterialsEnough;
  }
}
