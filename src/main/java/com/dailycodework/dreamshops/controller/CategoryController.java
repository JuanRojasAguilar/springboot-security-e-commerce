package com.dailycodework.dreamshops.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.category.CategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<ApiResponse> getAllCategories() {
    try {
      return ResponseEntity
          .ok()
          .body(
              new ApiResponse("Categories", categoryService.getAllCategories()));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", INTERNAL_SERVER_ERROR));
    }
  }

  public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
    try {
      Category theCategory = categoryService.addCategory(name);
      return ResponseEntity.ok(new ApiResponse("Success", theCategory));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok().body(new ApiResponse("Success", categoryService.getCategoryById(id)));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
    try {
      return ResponseEntity.ok().body(new ApiResponse("Success", categoryService.getCategoryByName(name)));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{id}/delete")
  public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
    try {
      categoryService.deleteCategoryById(id);
      return ResponseEntity.ok().body(new ApiResponse("Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

}
