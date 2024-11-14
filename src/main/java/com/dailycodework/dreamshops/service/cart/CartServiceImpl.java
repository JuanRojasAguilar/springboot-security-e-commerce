package com.dailycodework.dreamshops.service.cart;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dailycodework.dreamshops.dto.CartDto;
import com.dailycodework.dreamshops.dto.UserDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final AtomicLong cardIdGenerator = new AtomicLong(0);

  @Override
  public CartDto getCart(Long id) {
    Cart cart = cartRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    BigDecimal totalPrice = cart.getTotalPrice();
    cart.setTotalPrice(totalPrice);
    cart = cartRepository.save(cart);
    return converToDto(cart);
  }

  @Override
  @Transactional
  public void clearCart(Long id) {
    Cart cart = cartRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    cartItemRepository.deleteAllByCartId(id);
    cart.getCartItems().clear();
    cartRepository.deleteById(id);
  }

  @Override
  public BigDecimal getTotalPrice(Long id) {
    Cart cart = cartRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
    return cart.getTotalPrice();
  }

  @Override
  public CartDto initializeNewCart(UserDto user) {
    User userInstance = userRepository
        .findById(user.getId())
        .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

    return Optional
        .ofNullable(getCartByUserId(userInstance.getId()))
        .orElseGet(() -> {
          Cart cart = new Cart();
          cart.setUser(userInstance);
          cart = cartRepository.save(cart);
          return converToDto(cart);
        });
  }

  @Override
  public CartDto getCartByUserId(Long userId) {
    try {
      Cart cart = cartRepository.findByUserId(userId);
      return converToDto(cart);
    } catch (Exception e) {
      throw new ResourceNotFoundException("Cart Not Found");
    }
  }

  private CartDto converToDto(Cart cart) {
    return modelMapper.map(cart, CartDto.class);
  }
}
