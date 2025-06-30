package com.yms.user_service.service.interfaces;

import com.yms.user_service.dto.request.CreateUserRequest;
import com.yms.user_service.dto.request.UpdateUserRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.UserResponse;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.UUID;

public interface UserService {

    ApiResponse<String> createUser(CreateUserRequest createUserRequest);

    ApiResponse<String> updateUser(UpdateUserRequest createUserRequest);

    ApiResponse<String> deleteUser(UUID id);

    ApiResponse<UserResponse> getUser(UUID id);

    ApiResponse<List<UserResponse>> getAllUser();



}
