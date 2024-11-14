package com.dailycodework.dreamshops.requests;

import java.math.BigDecimal;

import com.dailycodework.dreamshops.model.Category;

import lombok.Data;

@Data
public class ProductRequest {
  private Long id;
  private String name;
  private String brand;
  private BigDecimal price;
  private Integer quantity;
  private String description;
  private Category category;
}
