package com.example.demo.transactionprocessing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.transactionprocessing.dto.LoginRequest;
import com.example.demo.transactionprocessing.dto.LoginResponse;
import com.example.demo.transactionprocessing.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        boolean authenticated = authService.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        if (authenticated) {
            return ResponseEntity.ok(
                    new LoginResponse(
                            true,
                            "Inicio de sesión correcto"
                    )
            );
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                    new LoginResponse(
                            false,
                            "Usuario o contraseña incorrectos"
                    )
                );
    }
}