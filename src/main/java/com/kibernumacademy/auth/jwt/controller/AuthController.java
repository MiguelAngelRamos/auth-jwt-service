package com.kibernumacademy.auth.jwt.controller;

import com.kibernumacademy.auth.jwt.dto.AuthUserDto;
import com.kibernumacademy.auth.jwt.dto.TokenDto;
import com.kibernumacademy.auth.jwt.entity.AuthUser;
import com.kibernumacademy.auth.jwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/create")
  public ResponseEntity<AuthUser> create(@RequestBody AuthUserDto authUserDto) {
    AuthUser authUser = authService.save(authUserDto);
    if(authUser == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(authUser);
  }
  public ResponseEntity<TokenDto> login(@RequestBody AuthUserDto authUserDto) {
    TokenDto tokenDto = authService.login(authUserDto);
    if(tokenDto == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(tokenDto);
  }

  public void validate() {

  }


}
