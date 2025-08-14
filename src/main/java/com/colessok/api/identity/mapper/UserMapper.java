package com.colessok.api.identity.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.colessok.api.identity.dto.request.user.UserAdminCreationRequest;
import com.colessok.api.identity.dto.request.user.UserAdminUpdateRequest;
import com.colessok.api.identity.dto.request.user.UserSelfCreationRequest;
import com.colessok.api.identity.dto.request.user.UserSelfUpdateRequest;
import com.colessok.api.identity.dto.response.user.UserAdminResponse;
import com.colessok.api.identity.dto.response.user.UserSelfResponse;
import com.colessok.api.identity.entity.User;
import com.colessok.api.identity.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserSelfCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    User toUserAdmin(UserAdminCreationRequest request);

    UserAdminResponse toUserAdminResponse(User user);

    UserSelfResponse toUserSelfResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUserAdmin(@MappingTarget User user, UserAdminUpdateRequest request);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "profile.avatar", ignore = true)
    void updateUserSelf(@MappingTarget User user, UserSelfUpdateRequest request);

    @AfterMapping
    default void linkUserAndProfile(@MappingTarget User user) {
        UserProfile profile = (user.getProfile() != null) ? user.getProfile() : new UserProfile();
        profile.setUser(user);
    }
}
