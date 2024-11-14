package com.dailycodework.dreamshops.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {
  private Long cartItemId;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;
  private ProductDto product;
}
