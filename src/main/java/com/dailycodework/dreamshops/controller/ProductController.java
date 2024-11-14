package com.dailycodework.dreamshops.controller;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.requests.ProductRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
  private final ProductService productService;

  @GetMapping
  public ResponseEntity<ApiResponse> getAllProducts() {
    List<ProductDto> products = productService.getAllProducts();
    return ResponseEntity.ok(new ApiResponse("success", products));
  }

  @GetMapping("/{id}/product")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
    try {
      ProductDto product = productService.getProductById(id);
      return ResponseEntity.ok(new ApiResponse("Success", product));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductRequest product) {
    try {
      ProductDto newProduct = productService.addProduct(product);
      return ResponseEntity.ok(new ApiResponse("Add Product ", newProduct));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/{id}/update")
  public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductRequest request, @PathVariable Long id) {
    try {
      ProductDto product = productService.updateProduct(request, id);
      return ResponseEntity.ok(new ApiResponse("Update success", product));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{id}/delete")
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
    try {
      productService.deleteProductById(id);
      return ResponseEntity.ok(new ApiResponse("Product deleted", id));
    } catch (Exception e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by/brand-and-name")
  public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName,
      @RequestParam String productName) {
    try {
      List<ProductDto> products = productService.getProductByBrandAndName(brandName, productName);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
      }
      return ResponseEntity.ok(new ApiResponse("Success", products));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by/category-and-brand")
  public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category,
      @RequestParam String brand) {
    try {
      List<ProductDto> products = productService.getProductsByCategoryAndBrand(category, brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
      }
      return ResponseEntity.ok(new ApiResponse("Success", products));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{name}/products")
  public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
    try {
      List<ProductDto> products = productService.getProductsByName(name);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
      }
      return ResponseEntity.ok(new ApiResponse("Success", products));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by-brand")
  public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand) {
    try {
      List<ProductDto> products = productService.getProductsByBrand(brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
      }
      return ResponseEntity.ok(new ApiResponse("Success", products));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{category}/all/products")
  public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable String category) {
    try {
      List<ProductDto> products = productService.getProductsByCategory(category);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
      }
      return ResponseEntity.ok(new ApiResponse("Success", products));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/count/by-brand-and-name")
  public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,
      @RequestParam String name) {
    try {
      Long productCount = productService.countProductsByBrandAndName(brand, name);
      return ResponseEntity.ok(new ApiResponse("Product Count", productCount));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
