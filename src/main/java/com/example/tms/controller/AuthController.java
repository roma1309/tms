package com.example.tms.controller;

import com.example.tms.config.security.JwtTokenUtils;
import com.example.tms.config.validation.ResponseErrorValidation;
import com.example.tms.dto.auth.JWTTokenResponse;
import com.example.tms.dto.auth.LoginRequest;
import com.example.tms.dto.auth.SignupRequest;
import com.example.tms.exceptions.ValidException;
import com.example.tms.service.UserService;
import com.example.tms.service.impl.CustomUserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final CustomUserServiceImpl customUserService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final ResponseErrorValidation responseErrorValidation;
    private final JwtTokenUtils jwtTokenUtils;

    @Autowired
    public AuthController(CustomUserServiceImpl customUserService, UserService userService, AuthenticationManager authenticationManager, ResponseErrorValidation responseErrorValidation, JwtTokenUtils jwtTokenUtils) {
        this.customUserService = customUserService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.responseErrorValidation = responseErrorValidation;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest,
                                               BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            throw new ValidException(HttpStatus.BAD_REQUEST, "Validation error", "Incorrectly filled in fields");
        }
        userService.createUser(signupRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<JWTTokenResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                             BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            throw new ValidException(HttpStatus.BAD_REQUEST, "Validation error", "Incorrectly filled in fields");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        UserDetails userDetails = customUserService.loadUserByUsername(loginRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JWTTokenResponse(token));
    }
}
