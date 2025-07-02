package com.yms.user_service.controllers;

import com.yms.user_service.dto.request.CreateModuleRequest;
import com.yms.user_service.dto.request.UpdateModuleRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.ModuleResponse;
import com.yms.user_service.service.interfaces.ModuleService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@GraphQLApi
@Service
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @GraphQLMutation(name = "createModule", description = "Create a new module")
    @PreAuthorize("hasAuthority('MODULE_CREATE')")
    public ApiResponse<String> createModule(@GraphQLArgument(name = "createModuleRequest") CreateModuleRequest createModuleRequest) {
       try {
           return moduleService.createModule(createModuleRequest);
       } catch (Exception e) {
           return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
       }
    }

    @GraphQLMutation(name = "updateModule", description = "Update an existing module")
    @PreAuthorize("hasAuthority('MODULE_UPDATE')")
    public ApiResponse<String> updateModule(@GraphQLArgument(name = "updateModuleRequest") UpdateModuleRequest updateModuleRequest) {
        try {
            return moduleService.updateModule(updateModuleRequest);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @GraphQLMutation(name = "deleteModule", description = "Delete a module by ID")
    @PreAuthorize("hasAuthority('MODULE_DELETE')")
    public ApiResponse<String> deleteModule(@GraphQLArgument(name = "id") UUID id) {
        try {
            return moduleService.deleteModule(id);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @GraphQLQuery(name = "getModule", description = "Get a module by ID")
    @PreAuthorize("hasAuthority('MODULE_VIEW')")
    public ApiResponse<ModuleResponse> getModule(@GraphQLArgument(name = "id") UUID id) {
        try {
            return moduleService.getModule(id);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @GraphQLQuery(name = "getAllModules", description = "Get all modules")
    @PreAuthorize("hasAuthority('MODULE_VIEW')")
    public ApiResponse<List<ModuleResponse>> getAllModules() {
        try {
            return moduleService.getAllModules();
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }
}
