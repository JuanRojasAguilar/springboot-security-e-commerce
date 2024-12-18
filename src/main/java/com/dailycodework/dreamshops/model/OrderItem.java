package com.dailycodework.dreamshops.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class OrderItem {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name="order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name="product_id")
  private Product product;
  private Integer quantity;
  private BigDecimal price;

  public OrderItem(Order order, Product product, Integer quantity, BigDecimal price) {
    this.order = order;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
  }
}
