package ca.ulaval.glo4002.reservation.context;

import ca.ulaval.glo4002.reservation.report.domain.material.Material;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.Map;

import static java.util.Map.entry;

public class DefaultMaterialValues {
    public static final Map<Material, Integer> MATERIAL_NEEDS_PER_CUSTOMER = Map.ofEntries(
            entry(new Material("knife", new Money(20.0d)), 3),
            entry(new Material("fork", new Money(20.0d)), 3),
            entry(new Material("spoon", new Money(20.0d)), 3),
            entry(new Material("bowl", new Money(170.0d)), 3),
            entry(new Material("plate", new Money(170.0d)), 3)
    );
    public static final Map<Material, Integer> MATERIAL_NEEDS_PER_RESTRICTION = Map.ofEntries(
            entry(new Material("knife", new Money(20.0d)), 0),
            entry(new Material("fork", new Money(20.0d)), 1),
            entry(new Material("spoon", new Money(20.0d)), 0),
            entry(new Material("bowl", new Money(170.0d)), 0),
            entry(new Material("plate", new Money(170.0d)), 1)
    );
    public static final Money COST_PER_WASH = new Money(100.0d);
    public static final int CAPACITY_PER_WASH = 9;
}
