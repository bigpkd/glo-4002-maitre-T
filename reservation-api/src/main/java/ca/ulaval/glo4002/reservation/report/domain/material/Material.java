package ca.ulaval.glo4002.reservation.report.domain.material;

import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.Objects;

public class Material {
    private String name;
    private Money cost;

    public String getName() {
        return name;
    }

    public Material(String name, Money cost) {
        this.name = name;
        this.cost = cost;
    }

    public Money getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material)) return false;
        Material material = (Material) o;
        return name.equals(material.name) &&
                cost.equals(material.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost);
    }
}
