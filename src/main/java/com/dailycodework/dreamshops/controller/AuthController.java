package com.dailycodework.dreamshops.controller;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.requests.LoginRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.response.JwtResponse;
import com.dailycodework.dreamshops.security.jwt.JwtUtils;
import com.dailycodework.dreamshops.security.user.ShopUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateTokenForUser(authentication);
      ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
      JwtResponse response = new JwtResponse(userDetails.getId(), jwt);
      return ResponseEntity.ok(new ApiResponse("Login Success", response));
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
