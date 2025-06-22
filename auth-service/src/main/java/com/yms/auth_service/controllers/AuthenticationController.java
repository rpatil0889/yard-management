package com.yms.auth_service.controllers;

import com.yms.auth_service.dto.request.AuthRequest;
import com.yms.auth_service.dto.request.UserRequest;
import com.yms.auth_service.dto.response.ApiResponse;
import com.yms.auth_service.dto.response.AuthResponse;
import com.yms.auth_service.services.interfaces.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * The Class AuthenticationController.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {

    /** The authentication service. */
    private final AuthenticationService authenticationService;


    /**
     * Registers a new user with the specified details.
     *
     * @param request the details of the user to be registered
     * @param bindingResult the result of the validation of the request
     * @return a ResponseEntity containing an ApiResponse with a message indicating the success or failure
     *         of the registration operation
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody UserRequest request, BindingResult bindingResult) {
        try {
            ApiResponse<String> apiResponse = authenticationService.registerUser(request, bindingResult);
            return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>>login(@Valid @RequestBody AuthRequest request, BindingResult result){
        try {
            ApiResponse<AuthResponse> login = authenticationService.login(request);
            return ResponseEntity.status(login.getStatus()).body(login);

        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token=authHeader;
            // Remove "Bearer "
            if (authHeader == null || authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            authenticationService.validateToken(token); // Throws exception if invalid

            return ResponseEntity.ok("Token is valid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

}
