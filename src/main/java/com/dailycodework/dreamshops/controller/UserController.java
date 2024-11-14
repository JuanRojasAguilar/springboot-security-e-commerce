package com.dailycodework.dreamshops.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.dto.UserDto;
import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.requests.CreateUserRequest;
import com.dailycodework.dreamshops.requests.UpdateUserRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
    try {
      UserDto user = userService.getUserById(userId);

      return ResponseEntity.ok(new ApiResponse("Success", user));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity
          .status(NOT_FOUND)
          .body(new ApiResponse(e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity
          .status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
    try {
      UserDto user = userService.createUser(request);
      return ResponseEntity.ok(new ApiResponse("Success", user));
    } catch (AlreadyExistsException e) {
      return ResponseEntity
          .status(CONFLICT)
          .body(new ApiResponse(e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity
          .status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/{userId}/update")
  public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId) {
    try {
      UserDto user = userService.updateUser(request, userId);

      return ResponseEntity
          .ok(new ApiResponse("Update Success", user));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity
          .status(NOT_FOUND)
          .body(new ApiResponse(e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity
          .status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
    try {
      userService.deleteUser(userId);
      return ResponseEntity.ok(new ApiResponse("User Deleted", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity
          .status(NOT_FOUND)
          .body(new ApiResponse(e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity
          .status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }
}
