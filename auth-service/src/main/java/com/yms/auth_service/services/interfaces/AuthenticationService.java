package com.yms.auth_service.services.interfaces;


import com.yms.auth_service.dto.request.AuthRequest;
import com.yms.auth_service.dto.request.UserRequest;
import com.yms.auth_service.dto.response.ApiResponse;
import com.yms.auth_service.dto.response.AuthResponse;

/**
 * The AuthenticationService interface provides methods for user authentication and authorization.
 */
public interface AuthenticationService {

    /**
     * Registers a new user with the given details.
     *
     * @param request The details of the user to be registered.
     * @param bindingResult The result of the validation of the request.
     * @return An ApiResponse containing a message indicating the success or failure
     *         of the registration operation.
     */
    ApiResponse<String> registerUser(UserRequest request);


    ApiResponse<AuthResponse>login(AuthRequest authRequest);


    ApiResponse<String>validateToken(String token);


}
