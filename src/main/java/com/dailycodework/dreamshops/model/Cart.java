package com.dailycodework.dreamshops.model;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  private BigDecimal totalPrice = BigDecimal.ZERO;

  @OneToMany(mappedBy="cart", cascade=CascadeType.ALL, orphanRemoval=true)
  private Set<CartItem> cartItems;

  @OneToOne
  @JoinColumn(name="user_id")
  private User user;

  public void addCartItem(CartItem item) {
    this.cartItems.add(item);
    item.setCart(this);
    updateTotalPrice();
  }

  public void removeCartItem(CartItem item) {
    this.cartItems.remove(item);
    item.setCart(null);
    updateTotalPrice();
  }

  public void updateTotalPrice() {
    this.totalPrice = cartItems.stream().map(item -> {
      BigDecimal unitPrice = item.getUnitPrice();
      if (unitPrice == null) {
        return BigDecimal.ZERO;
      }
      return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
    }).reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
