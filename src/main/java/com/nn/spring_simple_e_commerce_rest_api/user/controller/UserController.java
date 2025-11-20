package com.nn.spring_simple_e_commerce_rest_api.user.controller;

import com.nn.spring_simple_e_commerce_rest_api.shared.service.JwtService;
import com.nn.spring_simple_e_commerce_rest_api.user.api.request.LoginRequest;
import com.nn.spring_simple_e_commerce_rest_api.user.api.request.RegisterRequest;
import com.nn.spring_simple_e_commerce_rest_api.user.api.response.LoginResponse;
import com.nn.spring_simple_e_commerce_rest_api.user.domain.User;
import com.nn.spring_simple_e_commerce_rest_api.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        User authenticatedUser = userService.login(loginRequest);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginResponse);
    }
}
