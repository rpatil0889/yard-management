package com.yms.auth_service.controllers;

import com.yms.auth_service.dto.request.AuthRequest;
import com.yms.auth_service.dto.request.UserRequest;
import com.yms.auth_service.dto.response.ApiResponse;
import com.yms.auth_service.dto.response.AuthResponse;
import com.yms.auth_service.services.interfaces.AuthenticationService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * The Class AuthenticationController.
 */
@GraphQLApi
@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationController {

    /** The authentication service. */
    private final AuthenticationService authenticationService;


    /**
     * Registers a new user with the specified details.
     *
     * @param request the details of the user to be registered
     * @return a ResponseEntity containing an ApiResponse with a message indicating the success or failure
     *         of the registration operation
     */
    @GraphQLMutation(name = "registerUser", description = "Register a new user")
    public ApiResponse<String> registerUser(@GraphQLArgument(name = "request") UserRequest request) {
        try {
            return authenticationService.registerUser(request);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @GraphQLQuery(name = "login", description = "Login a user")
    public ApiResponse<AuthResponse>login(@Valid @RequestBody AuthRequest request){
        try {
            return authenticationService.login(request);

        }catch (Exception e){
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @GraphQLQuery(name = "validateToken", description = "Validate a JWT token")
    public ApiResponse<String> validateToken(@GraphQLArgument(name = "token") String token) {
        try {
            // Remove "Bearer "
            if (token == null || token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            authenticationService.validateToken(token); // Throws exception if invalid

            return new ApiResponse<>(HttpStatus.OK.value(), "Token is valid", null);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid Token", null);
        }
    }

}
