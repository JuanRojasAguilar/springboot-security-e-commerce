package com.dailycodework.dreamshops.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.dto.CartDto;
import com.dailycodework.dreamshops.dto.UserDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.CartItemService;
import com.dailycodework.dreamshops.service.cart.CartService;
import com.dailycodework.dreamshops.service.user.UserService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart/item")
public class CartItemController {
  private final CartItemService cartItemService;
  private final CartService cartService;
  private final UserService userService;

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addItemToCart(
      @RequestParam Long productId,
      @RequestParam Integer quantity) {
    try {
      UserDto user = userService.getAuthenticatedUser();
      CartDto cart = cartService.initializeNewCart(user);
      cartItemService.addCartItem(cart.getCartId(), productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    } catch (JwtException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/delete-from/{cartId}/item/{productId}")
  public ResponseEntity<ApiResponse> deleteItemFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
    try {
      cartItemService.removeItemFromCart(cartId, productId);
      return ResponseEntity.ok(new ApiResponse("Deleted", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/cart/{cartId}/item/{itemId}/update")
  public ResponseEntity<ApiResponse> updateItemQuantity(
      @PathVariable Long cartId,
      @PathVariable Long productId,
      @RequestParam Integer quantity) {
    try {
      cartItemService.updateItemQuantity(cartId, productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
