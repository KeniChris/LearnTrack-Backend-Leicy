package com.upc.learntrack.iam.controller;

import com.upc.learntrack.iam.dto.LoginRequestDto;
import com.upc.learntrack.iam.dto.RegisterRequestDto;
import com.upc.learntrack.iam.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDto request) {
        String token = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDto request) {
        long start = System.currentTimeMillis();
        log.info("[AUTH] LOGIN START email={}", request.getEmail());

        try {
            String token = authService.login(request);

            log.info("[AUTH] LOGIN OK email={} time={}ms", request.getEmail(), System.currentTimeMillis() - start);

            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            log.error("[AUTH] LOGIN FAIL email={} time={}ms error={}",
                    request.getEmail(),
                    System.currentTimeMillis() - start,
                    e.getMessage(),
                    e
            );
            throw e;
        }
    }
}