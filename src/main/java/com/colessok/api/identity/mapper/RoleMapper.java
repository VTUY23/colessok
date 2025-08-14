package com.colessok.api.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.colessok.api.identity.dto.request.RoleRequest;
import com.colessok.api.identity.dto.response.RoleResponse;
import com.colessok.api.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
