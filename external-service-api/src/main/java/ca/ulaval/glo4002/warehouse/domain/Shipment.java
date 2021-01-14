package ca.ulaval.glo4002.warehouse.domain;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Shipment {
  @Id
  @GeneratedValue
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ingredient_id", nullable = false)
  private Ingredient ingredient;

  @Column
  private String quantity;

  @Column
  private OffsetDateTime date;

  public Integer getId() {
    return id;
  }

  public String getQuantity() {
    return quantity;
  }

  public OffsetDateTime getDate() {
    return date;
  }
}
