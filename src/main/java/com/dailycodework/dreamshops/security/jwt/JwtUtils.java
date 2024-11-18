package com.dailycodework.dreamshops.security.jwt;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.dailycodework.dreamshops.security.user.ShopUserDetails;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  @Value("${auth.token.jwtSecret}")
  private String jwtSecret;
  @Value("${auth.token.expirationInMils}")
  private int expirationTime;

  public String generateTokenForUser(Authentication authentication) {
    ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();

    List<String> roles = userPrincipal
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    return Jwts.builder()
        .subject(userPrincipal.getEmail())
        .claim("id", userPrincipal.getId())
        .claim("roles", roles)
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + expirationTime))
        .signWith(key()).compact();
  }

  private SecretKey key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUserNameFromToken(String token) {
    return Jwts.parser()
      .verifyWith(key())
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
        .verifyWith(key())
        .build()
        .parseSignedClaims(token);
      return true;

    } catch (JwtException | IllegalArgumentException e) {
      throw new JwtException(e.getMessage());
    }
  }
}
