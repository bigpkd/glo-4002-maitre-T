package ca.ulaval.glo4002.reservation.report.servable.material;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4002.reservation.report.domain.material.Material;
import ca.ulaval.glo4002.reservation.report.domain.exception.UnknownMaterialException;
import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterialFactoryTest {
  private static final String UTENSIL_MATERIAL_NAME = "fork";
  private static final Money UTENSIL_MATERIAL_UNIT_PRICE = new Money(20);
  private static final String TABLEWARE_MATERIAL_NAME = "bowl";
  private static final Money TABLEWARE_MATERIAL_UNIT_PRICE = new Money(170);

  private MaterialFactory materialFactory;

  @BeforeEach
  void setUp() {
    materialFactory = new MaterialFactory();
  }

  @Test
  void whenCreatingUtensilsMaterial_thenMaterialIsCreatedWithAppropriatePrice() {
    Material material = materialFactory.create(UTENSIL_MATERIAL_NAME);

    assertTrue(UTENSIL_MATERIAL_UNIT_PRICE.equals(material.getCost()));
  }

  @Test
  void whenCreatingTablewareMaterial_thenMaterialIsCreatedWithAppropriatePrice() {
    Material material = materialFactory.create(TABLEWARE_MATERIAL_NAME);

    assertTrue(TABLEWARE_MATERIAL_UNIT_PRICE.equals(material.getCost()));
  }

  @Test
  void givenUnknownMaterialName_whenCreatingMaterial_thenUnknownMaterialExceptionIsThrown() {
    String unknownMaterialName = "Some random jiberish not being a material name";

    assertThrows(UnknownMaterialException.class, () -> materialFactory.create(unknownMaterialName));
  }
}