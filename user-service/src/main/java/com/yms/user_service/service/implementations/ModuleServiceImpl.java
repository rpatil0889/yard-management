package com.yms.user_service.service.implementations;

import com.yms.user_service.dto.request.CreateModuleRequest;
import com.yms.user_service.dto.request.UpdateModuleRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.ModuleResponse;
import com.yms.user_service.entities.RoleModule;
import com.yms.user_service.enums.ModuleStatus;
import com.yms.user_service.repositories.RoleModuleRepository;
import com.yms.user_service.service.interfaces.ModuleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final RoleModuleRepository moduleRepository;
    private final ModelMapper modelMapper;


    @Override
    public ApiResponse<String> createModule(CreateModuleRequest request) {
        if (request.getName() == null || request.getDescription() == null) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Module name and description are required", null);
        }
        if (moduleRepository.existsByName(request.getName())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Module name already exists", null);
        }
        RoleModule appModule = new RoleModule();
        appModule.setName(request.getName());
        appModule.setDescription(request.getDescription());
        appModule.setStatus(ModuleStatus.ACTIVE);
        appModule.setCreatedOn(LocalDateTime.now());
        moduleRepository.save(appModule);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Module created successfully", null);
    }

    @Override
    public ApiResponse<String> updateModule(UpdateModuleRequest request) {

        if (request.getId() == null) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Module id is required", null);
        }
        RoleModule module = moduleRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Module not found"));
        module.setName(request.getName());
        module.setDescription(request.getDescription());
        module.setStatus(ModuleStatus.valueOf(request.getStatus()));
        module.setUpdatedOn(LocalDateTime.now());
        moduleRepository.save(module);
        return new ApiResponse<>(HttpStatus.OK.value(), "Module updated successfully", null);
    }

    @Override
    public ApiResponse<String> deleteModule(UUID id) {
        RoleModule moduleNotFound = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        moduleNotFound.setStatus(ModuleStatus.DELETED);
        moduleRepository.save(moduleNotFound);
        return new ApiResponse<>(HttpStatus.OK.value(), "Module deleted successfully", null);
    }

    @Override
    public ApiResponse<ModuleResponse> getModule(UUID id) {
        RoleModule module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        ModuleResponse response = modelMapper.map(module, ModuleResponse.class);
        return new ApiResponse<>(HttpStatus.OK.value(), "Module found successfully",response);
    }

    @Override
    public ApiResponse<List<ModuleResponse>> getAllModules() {
        List<RoleModule> modules = moduleRepository.findAll();
        List<ModuleResponse> responseList = modules.stream().map(module -> modelMapper.map(module, ModuleResponse.class)).toList();
        return new ApiResponse<>(HttpStatus.OK.value(), "Modules found successfully",responseList);
    }
}
