package com.authentication.authentication_system.services;

import com.authentication.authentication_system.dto.*;

import java.util.UUID;

public interface UserService {

    Response register(RegisterRequest request);
    AuthTokens login(LoginRequest request);
    UserResponse getMe(UUID id);
    String refreshAccessToken(String refreshToken);
}
