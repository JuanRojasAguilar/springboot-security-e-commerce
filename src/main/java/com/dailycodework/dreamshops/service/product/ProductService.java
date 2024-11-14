package com.dailycodework.dreamshops.service.product;

import java.util.List;

import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.requests.ProductRequest;

public interface ProductService {
  ProductDto addProduct(ProductRequest request);
  ProductDto getProductById(Long id);
  void deleteProductById(Long id);
  ProductDto updateProduct(ProductRequest request, Long productId);

  List<ProductDto> getAllProducts();
  List<ProductDto> getProductsByCategory(String category);
  List<ProductDto> getProductsByBrand(String brand);
  List<ProductDto> getProductsByCategoryAndBrand(String category, String brand);
  List<ProductDto> getProductsByName(String name);
  List<ProductDto> getProductByBrandAndName(String brand, String name);

  Long countProductsByBrandAndName(String brand, String name);
}
