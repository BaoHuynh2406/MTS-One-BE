package com.mts.mtsone.modules.auth.service;

import com.mts.mtsone.modules.auth.dto.AuthenticationRequest;
import com.mts.mtsone.modules.auth.dto.AuthenticationResponse;
import com.mts.mtsone.modules.auth.dto.UserCreateDTO;

public interface AuthenticationService {
    AuthenticationResponse register(UserCreateDTO request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(String refreshToken);
} 