package com.yms.auth_service.services.interfaces;

import com.yms.auth_service.entities.User;

public interface JwtService {

    String generateToken(User user);

    void validateToken(final String token);

    String extractUsername(String token);
}
