package ca.ulaval.glo4002.reservation.report.domain.material;

import ca.ulaval.glo4002.reservation.utils.Money;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MaterialNeeds {
  private final Map<Material, Integer> materialNeedsPerCustomer;
  private final Map<Material, Integer> materialNeedsPerRestriction;

  public MaterialNeeds(Map<Material, Integer> materialNeedsPerCustomer,
                       Map<Material, Integer> materialNeedsPerRestriction) {
    this.materialNeedsPerCustomer = materialNeedsPerCustomer;
    this.materialNeedsPerRestriction = materialNeedsPerRestriction;
  }

  private HashMap<Material, Integer> countCustomerMaterialNeeds(int nbCustomers) {
    HashMap<Material, Integer> customerMaterialNeeds = new HashMap<>(materialNeedsPerCustomer);
    customerMaterialNeeds.replaceAll((key, value) -> nbCustomers * value);
    return customerMaterialNeeds;
  }

  private HashMap<Material, Integer> countRestrictionMaterialNeeds(int nbRestrictions) {
    HashMap<Material, Integer> restrictionMaterialNeeds = new HashMap<>(materialNeedsPerRestriction);
    restrictionMaterialNeeds.replaceAll((key, value) -> nbRestrictions * value);
    return restrictionMaterialNeeds;
  }

  public HashMap<Material, Integer> countMaterialNeeds(int nbCustomers, int nbRestrictions) {
    HashMap<Material, Integer> materialNeeds = new HashMap<>();

    if (nbCustomers > 0) {
      HashMap<Material, Integer> customerMaterialNeeds = countCustomerMaterialNeeds(nbCustomers);
      HashMap<Material, Integer> restrictionMaterialNeeds = countRestrictionMaterialNeeds(nbRestrictions);
      materialNeeds.putAll(customerMaterialNeeds);

      for (Map.Entry<Material, Integer> entry : restrictionMaterialNeeds.entrySet()) {
        if (materialNeeds.containsKey(entry.getKey())) {
          materialNeeds.replace(entry.getKey(), materialNeeds.get(entry.getKey()) + entry.getValue());
        } else {
          materialNeeds.put(entry.getKey(), entry.getValue());
        }
      }
    }

    return materialNeeds;
  }

  public Map<Material,Integer> countMaterialsToBuy(Map<Material,Integer> requiredMaterials, Map<Material,Integer> availableMaterials){
    HashMap<Material, Integer> materialsToBuy = new HashMap<>(requiredMaterials);

    for (Map.Entry<Material, Integer> entry : availableMaterials.entrySet()) {
      int quantityToBuy = 0;
      Material availableMaterial = entry.getKey();
      Integer availableQuantity = entry.getValue();

      if(materialsToBuy.containsKey(availableMaterial)) {
        Integer requiredQuantity = materialsToBuy.get(availableMaterial);
        quantityToBuy = (requiredQuantity > availableQuantity) ? requiredQuantity - availableQuantity : 0;
        materialsToBuy.replace(availableMaterial, quantityToBuy);
      }
    }

    if (materialsToBuy.values().stream().reduce(0, Integer::sum) == 0) {
      materialsToBuy.clear();
    }

    return materialsToBuy;
  }

  public Money computeCostOfMaterials(Map<Material,Integer> materials){
    Money materialCost = new Money(0.0d);

    for (Map.Entry<Material, Integer> entry : materials.entrySet()) {
      materialCost = materialCost.add(entry.getKey().getCost().multiply(BigDecimal.valueOf(entry.getValue())));
    }

    return materialCost;
  }
}
