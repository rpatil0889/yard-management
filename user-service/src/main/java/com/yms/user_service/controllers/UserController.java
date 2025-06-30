package com.yms.user_service.controllers;

import com.yms.user_service.dto.request.CreateUserRequest;
import com.yms.user_service.dto.request.UpdateUserRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.UserResponse;
import com.yms.user_service.service.interfaces.UserService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@GraphQLApi
@Service
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GraphQLMutation(name = "createUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> createUser(@GraphQLArgument(name = "createUserRequest") CreateUserRequest createUserRequest) {

        log.info("Create new user Request {}", createUserRequest);

        try {
            return userService.createUser(createUserRequest);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GraphQLMutation(name = "updateUser", description = "Update a user")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> updateUser(@GraphQLArgument(name = "updateUserRequest") UpdateUserRequest updateUserRequest) {
        log.info("Update existing user request {}", updateUserRequest);
        try {
            return userService.updateUser(updateUserRequest);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GraphQLMutation(name = "deleteUser", description = "Delete a user by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteUser(@GraphQLArgument(name = "id") UUID id) {
        log.info("Delete user request {}", id);
        try {
            return userService.deleteUser(id);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GraphQLQuery(name = "getUser", description = "Get a user by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> getUser(@GraphQLArgument(name = "id") UUID id) {
        log.info("Get user by Id request {}", id);
        try {
            return userService.getUser(id);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GraphQLQuery(name = "listUsers", description = "List all users")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> listUsers() {
        log.info("Get all user list request");
        try {
            return userService.getAllUser();
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

}
