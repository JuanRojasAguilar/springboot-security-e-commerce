package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.dto.CartItemDto;

public interface CartItemService {
  void addCartItem(Long cartId, Long productId, int quantity);
  void removeItemFromCart(Long cartId, Long productId);
  void updateItemQuantity(Long cartId, Long productId, int quantity);
  CartItemDto getCartItem(Long cartId, Long productId);
}
