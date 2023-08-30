package com.kibernumacademy.auth.jwt.repository;

import com.kibernumacademy.auth.jwt.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAuthUserRepository extends JpaRepository<AuthUser, Integer> {

  Optional<AuthUser> findByUserName(String userName);
}
