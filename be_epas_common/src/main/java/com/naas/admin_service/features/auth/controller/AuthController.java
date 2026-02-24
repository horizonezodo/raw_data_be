package com.naas.admin_service.features.auth.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.naas.admin_service.features.auth.dto.KeycloakLoginResposeDto;
import com.naas.admin_service.features.auth.dto.LoginByCodeDto;
import com.naas.admin_service.features.auth.dto.LoginDto;
import com.naas.admin_service.features.auth.dto.RefreshTokenDto;
import com.naas.admin_service.features.auth.dto.SecretDto;
import com.naas.admin_service.features.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto){
            Object object = authService.login(loginDto);
            return ResponseEntity.ok(object);
    }

    @PostMapping("/exchange-code")
    public ResponseEntity<Object> exchangeCode(@RequestBody LoginByCodeDto dto){
        Object object = authService.exchangeCode(dto.getCode());
        return ResponseEntity.ok(object);
    }


    @PostMapping("/token")
    public ResponseEntity<Object> loginClientSecret(@RequestBody SecretDto secretDto){
        Object object = authService.loginScret(secretDto);
        return ResponseEntity.ok(object);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@RequestBody RefreshTokenDto refreshToken){
        Object object = authService.refreshToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok(object);
    }

    @GetMapping("/info-user")
    public ResponseEntity<Object> getProfileUser(){
        Object user = authService.getProfileUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-kc-login-url")
    public ResponseEntity<ResponseData<KeycloakLoginResposeDto>> getLoginUrl() {
        return ResponseData.okEntity(authService.getLoginUrl());
    }

}
