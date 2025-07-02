package com.yms.auth_service.services.implementations;

import com.yms.auth_service.entities.Permission;
import com.yms.auth_service.entities.RoleModule;
import com.yms.auth_service.entities.RoleModulePermission;
import com.yms.auth_service.entities.User;
import com.yms.auth_service.services.interfaces.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final long EXPIRATION = 1000 * 60 * 30; // 30 mins
    
    @Override
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        List<String> authorities = getUserPermissions(user);

        claims.put("authorities", authorities);
        claims.put("id", user.getId());
        claims.put("name", user.getName());

        return createToken(claims, user.getEmail());
    }

    @Override
    public void validateToken(String token) {

        Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {

        byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(SECRET.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }
    @Override
    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private List<String> getUserPermissions(User user) {

        if (user == null || user.getRoles() == null) {
            return Collections.emptyList();
        }

        return user.getRoles().stream()
                .filter(Objects::nonNull)
                .flatMap(role -> {
                    List<RoleModulePermission> modulePermissions = role.getModulePermissions();
                    return modulePermissions == null ? Stream.empty() : modulePermissions.stream();
                })
                .filter(Objects::nonNull)
                .map(rmp -> {
                    RoleModule module = rmp.getModule();
                    Permission permission = rmp.getPermission();
                    if (module != null && permission != null) {
                        return (module.getName() + "_" + permission.getName()).toUpperCase();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .distinct() // avoid duplicates
                .collect(Collectors.toList());
    }
}
