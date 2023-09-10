package com.sirius.mailSender.controllers;

import com.sirius.mailSender.dtos.AuthResponseDTO;
import com.sirius.mailSender.dtos.LoginDTO;
import com.sirius.mailSender.dtos.RegisterDTO;
import com.sirius.mailSender.repositories.RoleRepository;
import com.sirius.mailSender.repositories.UserEntityRepository;
import com.sirius.mailSender.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody RegisterDTO registerDTO){
        try {
            authService.register(registerDTO);
            return new ResponseEntity<>("User registered", HttpStatus.CREATED);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginDTO loginDTO){
        try {
            String token = authService.login(loginDTO);
            return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
