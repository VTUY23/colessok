package com.colessok.api.identity.service;

import java.io.IOException;
import java.util.HashSet;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.common.constant.PredefinedRole;
import com.colessok.api.common.dto.PageResponse;
import com.colessok.api.common.exception.AppException;
import com.colessok.api.common.exception.ErrorCode;
import com.colessok.api.file.entity.File;
import com.colessok.api.file.repository.FileMgmtRepository;
import com.colessok.api.file.service.FileService;
import com.colessok.api.identity.dto.request.user.*;
import com.colessok.api.identity.dto.response.user.*;
import com.colessok.api.identity.entity.Role;
import com.colessok.api.identity.entity.User;
import com.colessok.api.identity.entity.UserProfile;
import com.colessok.api.identity.mapper.UserMapper;
import com.colessok.api.identity.repository.RoleRepository;
import com.colessok.api.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    FileService fileService;
    FileMgmtRepository fileMgmtRepository;

    public UserSelfResponse createUser(UserSelfCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setEmailVerified(false);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserSelfResponse(user);
    }

    public UserSelfResponse getMyInfo() throws IOException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var response = userMapper.toUserSelfResponse(user);

        var avatar = user.getProfile().getAvatar();
        if (avatar != null) response.getProfile().setAvatar(fileService.getFileInfo(avatar));
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserAdminResponse createUserAdmin(UserAdminCreationRequest request) {
        User user = userMapper.toUserAdmin(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        user.setEmailVerified(false);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserAdminResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserAdminResponse updateUserAdmin(String userId, UserAdminUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUserAdmin(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserAdminResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserAdminResponse> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        var pageData = userRepository.findAll(pageable);

        var userList = pageData.stream().map(userMapper::toUserAdminResponse).toList();

        return PageResponse.<UserAdminResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(userList)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserAdminResponse getUser(String id) {
        return userMapper.toUserAdminResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public void changePassword(ChangePasswordRequest request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }
        String newPassword = passwordEncoder.encode(request.getNewPassword());
        if (passwordEncoder.matches(request.getCurrentPassword(), newPassword)) {
            throw new AppException(ErrorCode.PASSWORD_MUST_CHANGE);
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public UserSelfResponse updateMyInfo(UserSelfUpdateRequest request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUserSelf(user, request);

        return userMapper.toUserSelfResponse(userRepository.save(user));
    }

    public UserSelfResponse updateAvatar(MultipartFile file) throws IOException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        File avatar = fileService.uploadAndSave(file);

        if (user.getProfile() == null) {
            user.setProfile(new UserProfile());
        }
        user.getProfile().setAvatar(avatar);

        return userMapper.toUserSelfResponse(userRepository.save(user));
    }

    @Transactional
    public void updateUserSelf(UserSelfUpdateRequest request) throws IOException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUserSelf(user, request);

        UserProfile profile = user.getProfile();
        File oldAvatar = profile.getAvatar();

        MultipartFile avatarFile = request.getProfile().getAvatar();
        if (avatarFile != null && !avatarFile.isEmpty()) {

            // Xóa avatar cũ nếu có
            if (oldAvatar != null) fileMgmtRepository.delete(oldAvatar);

            // // Lưu avatar mới
            profile.setAvatar(fileService.uploadAndSave(avatarFile));
        } else if (avatarFile == null && oldAvatar != null) {
            // Xóa avatar nếu không gửi file mới
            fileMgmtRepository.delete(oldAvatar);
            profile.setAvatar(null);
        }

        // Lưu user (cascade với profile)
        userRepository.save(user);
    }
}
