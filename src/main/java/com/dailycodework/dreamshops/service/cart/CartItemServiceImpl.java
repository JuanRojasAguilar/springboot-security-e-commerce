package com.dailycodework.dreamshops.service.cart;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.dto.CartItemDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
  private final CartItemRepository cartItemRepository;
  private final CartRepository cartRepository;
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;

  @Override
  public void addCartItem(Long cartId, Long productId, int quantity) {
    // 1. Get the cart
    // 2. Get the product
    Cart cart = cartRepository
        .findById(cartId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

    Product product = productRepository
        .findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product non existent"));

    // 3. Check if product exists in car
    CartItem cartItem = null;
    try {
      cartItem = getCartItemByCartAndProduct(cartId, productId);
      cartItem.setQuantity(cartItem.getQuantity() + quantity);

    } catch (RuntimeException e) {
      cartItem = new CartItem();
      cartItem.setProduct(product);
      cartItem.setQuantity(quantity);
      cartItem.setCart(cart);
      cartItem.setUnitPrice(product.getPrice());
    }

    cartItem.setTotalPrice();
    cart.setCartItem(cartItem);
    cartItemRepository.save(cartItem);
    cartRepository.save(cart);
  }

  @Override
  public void removeItemFromCart(Long cartId, Long productId) {
    Cart cart = cartRepository
        .findById(cartId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart Not found"));

    CartItem itemToRemove = getCartItemByCartAndProduct(cartId, productId);
    cart.removeCartItem(itemToRemove);
    cartRepository.save(cart);
  }

  @Override
  public void updateItemQuantity(Long cartId, Long productId, int quantity) {
    Cart cart = cartRepository
        .findById(cartId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
    CartItem cartItem = getCartItemByCartAndProduct(cartId, productId);

    cartItem.setQuantity(quantity);
    cartItem.setUnitPrice(cartItem.getProduct().getPrice());
    cartItem.setTotalPrice();

    cart.updateTotalPrice();
    cartRepository.save(cart);
  }

  private CartItem getCartItemByCartAndProduct(Long cartId, Long productId) {
    Cart cart = cartRepository
        .findById(cartId)
        .orElseThrow(() -> new ResourceNotFoundException("Not an Existing Cart"));

    return cart.getCartItems().stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
  }

  @Override
  public CartItemDto getCartItem(Long cartId, Long productId) {
    CartItem cartItem = getCartItemByCartAndProduct(cartId, productId);
    return convertToDto(cartItem);
  }

  private CartItemDto convertToDto(CartItem cartItem) {
    return modelMapper.map(cartItem, CartItemDto.class);
  }
}
