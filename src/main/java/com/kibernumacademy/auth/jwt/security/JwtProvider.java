package com.kibernumacademy.auth.jwt.security;

import com.kibernumacademy.auth.jwt.entity.AuthUser;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
  @Value("${jwt.secret}")
  private String secret;

  @PostConstruct
  protected void init() {
    secret = Base64.getEncoder().encodeToString(secret.getBytes());
    // cualquier uso de la variable secret estará codificada en base64
  }

  public String createToken(AuthUser authUser) {
    Map<String, Object> claims = new HashMap<>();
    claims = Jwts.claims().setSubject(authUser.getUserName());
    claims.put("id", authUser.getId());
    Date now = new Date();
    Date expirationToken = new Date(now.getTime() + 3600000);
    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expirationToken)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      return true;
    } catch(Exception exception) {
      return false;
    }
  }

  public String getUserNameFromToken(String token) {
    try {
      return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject(); // el nombre usuario
    } catch(Exception exception) {
      return "Bad Token";
    }
  }
}