package com.colessok.api.identity.mapper;

import org.mapstruct.Mapper;

import com.colessok.api.identity.dto.request.PermissionRequest;
import com.colessok.api.identity.dto.response.PermissionResponse;
import com.colessok.api.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
