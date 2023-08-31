package com.kibernumacademy.auth.jwt.service;

import com.kibernumacademy.auth.jwt.dto.AuthUserDto;
import com.kibernumacademy.auth.jwt.dto.TokenDto;
import com.kibernumacademy.auth.jwt.entity.AuthUser;
import com.kibernumacademy.auth.jwt.repository.IAuthUserRepository;
import com.kibernumacademy.auth.jwt.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

  @Autowired
  private IAuthUserRepository authUserRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtProvider jwtProvider;

  // Crear el Usuario
  public AuthUser save (AuthUserDto dto) {
    Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
    if(user.isPresent()) {
      return null;
    }
    String password = passwordEncoder.encode(dto.getPassword()); // academyjava -> 84FJFJAFHFHHFEFE3838FH

    AuthUser authUser = AuthUser.builder()
            .userName(dto.getUserName())
            .password(password)
            .build();
    return authUserRepository.save(authUser);
  }

  // Login
  public TokenDto login(AuthUserDto dto) {
    Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());

    if(!user.isPresent()) {
      return null; // si no está presente es lo mismo decir que no está registrado
    }
    // vamos la contraseña y la vamos a comparar con lo que tenemos en la persistencia
    if(passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
      return new TokenDto(jwtProvider.createToken(user.get()));
    }
    return null;
  }
  // Validar el token
  public TokenDto validate(String token) {

    if(!jwtProvider.validateToken(token)) {
      return null;
    }
    String userName = jwtProvider.getUserNameFromToken(token);

    // Validar que ese nombre de usuario exista en la persistencia
    if(!authUserRepository.findByUserName(userName).isPresent()) {
      return null;
    }
    return new TokenDto(token);
  }
}
