package ca.ulaval.glo4002.reservation.report.servable.material;

import ca.ulaval.glo4002.reservation.report.domain.material.Material;
import ca.ulaval.glo4002.reservation.report.domain.exception.UnknownMaterialException;
import ca.ulaval.glo4002.reservation.utils.Money;

public class MaterialFactory {

  public Material create(String materialName) {
    int price = 0;
    switch (materialName) {
      case "spoon":
      case "knife":
      case "fork": {
        price = 20;
        break;
      }

      case "plate":
      case "bowl": {
        price = 170;
        break;
      }

      default: {
        throw new UnknownMaterialException();
      }
    }

    Money unitPrice = new Money(price);
    return new Material(materialName, unitPrice);
  }
}
