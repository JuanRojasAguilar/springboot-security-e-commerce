package com.dailycodework.dreamshops.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {
  private Long id;
  private Long userId;
  private LocalDate orderDate;
  private BigDecimal orderTotal;
  private String status;
  private List<OrderItemDto> orderItems;
}