package com.dailycodework.dreamshops.controller;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.dto.CartDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.CartService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/shoppingCart")
public class CartController {
  private final CartService cartService;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getCart(@PathVariable Long id) {
    try {
      CartDto cart = cartService.getCart(id);
      return ResponseEntity.ok(new ApiResponse("Success", cart));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{id}/delete")
  public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id) {
    try {
    cartService.clearCart(id);
    return ResponseEntity.ok(new ApiResponse("Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{id}/total-price")
  public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long id) {
    BigDecimal totalPrice = cartService.getTotalPrice(id);
    return ResponseEntity.ok(new ApiResponse("Success", totalPrice));
  }
}
