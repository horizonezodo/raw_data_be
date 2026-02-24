package com.naas.admin_service.features.auth.service;

import com.naas.admin_service.features.auth.dto.KeycloakLoginResposeDto;
import com.naas.admin_service.features.auth.dto.LoginDto;
import com.naas.admin_service.features.auth.dto.SecretDto;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;

import java.util.List;

public interface AuthService {
    Object login(LoginDto loginDto);

    Object exchangeCode(String code);

    Object loginScret(SecretDto secretDto);

    Object refreshToken(String refreshToken);

    Object getProfileUser();

    List<ListResourceMappingDto> getListByUsername(String username);

    KeycloakLoginResposeDto getLoginUrl();

}
