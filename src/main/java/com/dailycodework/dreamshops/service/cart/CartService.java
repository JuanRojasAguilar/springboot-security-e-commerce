package com.dailycodework.dreamshops.service.cart;

import java.math.BigDecimal;

import com.dailycodework.dreamshops.dto.CartDto;
import com.dailycodework.dreamshops.dto.UserDto;

public interface CartService {
  CartDto getCart(Long id);
  void clearCart(Long id);
  BigDecimal getTotalPrice(Long id);
  CartDto initializeNewCart(UserDto user);

  CartDto getCartByUserId(Long id);
}
