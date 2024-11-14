package com.dailycodework.dreamshops.dto;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Data;

@Data
public class CartDto {
  private Long cartId;
  private BigDecimal totalPrice;
  private Set<CartItemDto> items;
}
