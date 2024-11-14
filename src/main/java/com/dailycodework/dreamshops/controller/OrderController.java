package com.dailycodework.dreamshops.controller;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.order.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
  private final OrderService orderService;

  @PostMapping("/place/{userId}")
  public ResponseEntity<ApiResponse> createOrder(@PathVariable Long userId) {
    try {
      OrderDto order = orderService.placeOrder(userId);
      return ResponseEntity.ok(new ApiResponse("Order placed", order));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
    try {
      OrderDto order = orderService.getOrder(orderId);
      return ResponseEntity.ok(new ApiResponse("Success", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity
          .status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{userId}/orders")
  public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
    try {
      List<OrderDto> orders = orderService.getUserOrders(userId);
      return ResponseEntity.ok(new ApiResponse("Success", orders));
    } catch (Exception e) {
      return ResponseEntity
          .status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse(e.getMessage(), null));
    }

  }
}
