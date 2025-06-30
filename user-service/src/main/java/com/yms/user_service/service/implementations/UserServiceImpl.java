package com.yms.user_service.service.implementations;

import com.yms.user_service.dto.request.CreateUserRequest;
import com.yms.user_service.dto.request.UpdateUserRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.UserResponse;
import com.yms.user_service.entities.Role;
import com.yms.user_service.entities.User;
import com.yms.user_service.enums.RoleStatus;
import com.yms.user_service.enums.UserStatus;
import com.yms.user_service.repositories.RoleRepository;
import com.yms.user_service.repositories.UserRepository;
import com.yms.user_service.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<String> createUser(CreateUserRequest createUserRequest) {

        Optional<User> user = userRepository.findByEmailAndStatus(createUserRequest.getEmail(), UserStatus.ACTIVE);
        if (user.isPresent()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "User already exists", null);
        }

       User userEntity = User.builder()
               .name(createUserRequest.getName())
               .email(createUserRequest.getEmail())
               .phone(createUserRequest.getPhone())
               .status(UserStatus.ACTIVE)
               .build();
        Set<Role> roles = roleRepository.findByNameInAndStatus(createUserRequest.getRoles(), RoleStatus.ACTIVE);
        userEntity.setRoles(roles);
        userEntity.setCreatedOn(LocalDateTime.now());
        userRepository.save(userEntity);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "User created successfully", null);
    }

    @Override
    public ApiResponse<String> updateUser(UpdateUserRequest createUserRequest) {

        Optional<User> optionalUser = userRepository.findById(createUserRequest.getId());
        if (!optionalUser.isPresent()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "User not found", null);
        }
        User userEntity = optionalUser.get();
        userEntity.setEmail(createUserRequest.getEmail());
        userEntity.setPhone(createUserRequest.getPhone());
        userEntity.setStatus(UserStatus.valueOf(createUserRequest.getStatus()));
        userEntity.setUpdatedOn(LocalDateTime.now());
        userRepository.save(userEntity);

        return new ApiResponse<>(HttpStatus.OK.value(), "User updated successfully", null);
    }

    @Override
    public ApiResponse<String> deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.DELETED);
        user.setUpdatedOn(LocalDateTime.now());
        userRepository.save(user);
        return new ApiResponse<>(HttpStatus.OK.value(), "User deleted successfully", null);
    }

    @Override
    public ApiResponse<UserResponse> getUser(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            return new ApiResponse<>(HttpStatus.OK.value(), "User found successfully", userResponse);
        }
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "User not found", null);
    }

    @Override
    public ApiResponse<List<UserResponse>> getAllUser() {
        List<User> users = userRepository.findAll();
        List<UserResponse> responseList = users.stream().map(user -> modelMapper.map(user, UserResponse.class)).toList();
        return new ApiResponse<>(HttpStatus.OK.value(), "Users found successfully",responseList);
    }

}
