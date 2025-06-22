package com.yms.auth_service.services.implementations;

import com.yms.auth_service.dto.request.AuthRequest;
import com.yms.auth_service.dto.request.UserRequest;
import com.yms.auth_service.dto.response.ApiResponse;
import com.yms.auth_service.dto.response.AuthResponse;
import com.yms.auth_service.entities.Role;
import com.yms.auth_service.entities.User;
import com.yms.auth_service.enums.UserStatus;
import com.yms.auth_service.repositories.RoleRepository;
import com.yms.auth_service.repositories.UserRepository;
import com.yms.auth_service.services.interfaces.AuthenticationService;
import com.yms.auth_service.services.interfaces.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The authentication service implementation.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    /** The user repository. */
    private final UserRepository userRepository;

    /** The role repository. */
    private final RoleRepository roleRepository;

    /** The model mapper. */
    private final ModelMapper modelMapper;

    /** The Password Encode */
    private final PasswordEncoder passwordEncoder;

    /** The JWT Service */
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     *
     * @return a string with a success message.
     */
    @Override
    public ApiResponse<String> registerUser(UserRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.append(error.getField())
                            .append(": ")
                            .append(error.getDefaultMessage())
                            .append("; ")
            );
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), errors.toString(), null);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "User already exists", null);
        }
        User user = modelMapper.map(request, User.class);
        List<Role> roles = roleRepository.findByNameIn(List.of("USER"));
        user.setRole(roles);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedOn(LocalDateTime.now());
        userRepository.save(user);

        return new ApiResponse<>(HttpStatus.OK.value(), "User registered successfully", null);
    }

    @Override
    public ApiResponse<AuthResponse> login(AuthRequest authRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                authRequest.getPassword()));

        if (authenticate.isAuthenticated()){
            Optional<User> optionalUser = userRepository.findByEmailAndStatus(authRequest.getEmail(), UserStatus.ACTIVE);
            if (!optionalUser.isEmpty()) {
                User user = optionalUser.get();
                String token = jwtService.generateToken(user);
                AuthResponse authResponse = AuthResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .token(token)
                        .role(user.getRole().stream().map(Role::getName).toList()).build();
                return new ApiResponse<>(HttpStatus.OK.value(), "Login Successful", authResponse);
            }
            else {
                return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "User Not Found", null);
            }
        }
        else return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(),"UnAuthorized",null);

    }

    @Override
    public ApiResponse<String> validateToken(String token) {
        jwtService.validateToken(token);
        return new ApiResponse<>(HttpStatus.OK.value(),"Token is valid",null);
    }
}
