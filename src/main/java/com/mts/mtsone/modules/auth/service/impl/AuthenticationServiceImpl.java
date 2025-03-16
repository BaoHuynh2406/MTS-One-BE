package com.mts.mtsone.modules.auth.service.impl;

import com.mts.mtsone.common.exception.BusinessException;
import com.mts.mtsone.modules.auth.dto.AuthenticationRequest;
import com.mts.mtsone.modules.auth.dto.AuthenticationResponse;
import com.mts.mtsone.modules.auth.dto.UserCreateDTO;
import com.mts.mtsone.modules.auth.dto.UserDTO;
import com.mts.mtsone.modules.auth.entity.User;
import com.mts.mtsone.modules.auth.mapper.UserMapper;
import com.mts.mtsone.modules.auth.security.JwtService;
import com.mts.mtsone.modules.auth.service.AuthenticationService;
import com.mts.mtsone.modules.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    @Override
    @Transactional
    public AuthenticationResponse register(UserCreateDTO request) {
        UserDTO userDTO = userService.createUser(request);
        String accessToken = jwtService.generateToken(mapToUserDetails(userDTO));
        String refreshToken = jwtService.generateRefreshToken(mapToUserDetails(userDTO));
        
        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(jwtExpiration / 1000)
            .user(userDTO)
            .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDTO userDTO = userService.getUserByUsername(userDetails.getUsername());
        
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        userService.updateLastLogin(userDTO.getId());
        
        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(jwtExpiration / 1000)
            .user(userDTO)
            .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        final String username = jwtService.extractUsername(refreshToken);
        if (username == null) {
            throw new BusinessException("INVALID_TOKEN", "Token không hợp lệ");
        }

        UserDTO userDTO = userService.getUserByUsername(username);
        UserDetails userDetails = mapToUserDetails(userDTO);
        
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new BusinessException("INVALID_TOKEN", "Token không hợp lệ");
        }

        String accessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);
        
        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(newRefreshToken)
            .expiresIn(jwtExpiration / 1000)
            .user(userDTO)
            .build();
    }

    private UserDetails mapToUserDetails(UserDTO userDTO) {
        return org.springframework.security.core.userdetails.User.builder()
            .username(userDTO.getUsername())
            .password("") // Không cần password vì chỉ dùng để tạo token
            .authorities(userDTO.getRoles().stream()
                .map(role -> "ROLE_" + role)
                .toArray(String[]::new))
            .build();
    }
} 