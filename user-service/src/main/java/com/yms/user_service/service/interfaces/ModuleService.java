package com.yms.user_service.service.interfaces;

import com.yms.user_service.dto.request.CreateModuleRequest;
import com.yms.user_service.dto.request.UpdateModuleRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.ModuleResponse;

import java.util.List;
import java.util.UUID;

public interface ModuleService {

    ApiResponse<String> createModule(CreateModuleRequest moduleRequest);

    ApiResponse<String> updateModule(UpdateModuleRequest moduleRequest);

    ApiResponse<String> deleteModule(UUID id);

    ApiResponse<ModuleResponse> getModule(UUID id);

    ApiResponse<List<ModuleResponse>> getAllModules();

}
