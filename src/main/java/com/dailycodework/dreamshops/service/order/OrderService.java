package com.dailycodework.dreamshops.service.order;

import java.util.List;

import com.dailycodework.dreamshops.dto.OrderDto;

public interface OrderService {
  OrderDto placeOrder(Long userId);
  OrderDto getOrder(Long orderId);
  List<OrderDto> getUserOrders(Long userId);
}
