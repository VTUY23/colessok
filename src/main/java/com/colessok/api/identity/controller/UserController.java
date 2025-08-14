package com.colessok.api.identity.controller;

import java.io.IOException;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.common.dto.ApiResponse;
import com.colessok.api.common.dto.PageResponse;
import com.colessok.api.identity.dto.request.user.ChangePasswordRequest;
import com.colessok.api.identity.dto.request.user.UserAdminCreationRequest;
import com.colessok.api.identity.dto.request.user.UserAdminUpdateRequest;
import com.colessok.api.identity.dto.request.user.UserSelfCreationRequest;
import com.colessok.api.identity.dto.request.user.UserSelfUpdateRequest;
import com.colessok.api.identity.dto.response.user.UserAdminResponse;
import com.colessok.api.identity.dto.response.user.UserSelfResponse;
import com.colessok.api.identity.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/registration")
    ApiResponse<UserSelfResponse> createUser(@RequestBody @Valid UserSelfCreationRequest request) {
        return ApiResponse.<UserSelfResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PostMapping
    ApiResponse<UserAdminResponse> createUserAdmin(@RequestBody @Valid UserAdminCreationRequest request) {
        return ApiResponse.<UserAdminResponse>builder()
                .result(userService.createUserAdmin(request))
                .build();
    }

    @GetMapping
    ApiResponse<PageResponse<UserAdminResponse>> getUsers(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<UserAdminResponse>>builder()
                .result(userService.getUsers(page, size))
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserAdminResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserAdminResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserSelfResponse> getMyInfo() throws IOException {
        return ApiResponse.<UserSelfResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserAdminResponse> updateUserAdmin(
            @PathVariable String userId, @RequestBody @Valid UserAdminUpdateRequest request) {
        return ApiResponse.<UserAdminResponse>builder()
                .result(userService.updateUserAdmin(userId, request))
                .build();
    }

    @PutMapping("/my-info")
    ApiResponse<UserSelfResponse> updateUserSelf(@RequestBody @Valid UserSelfUpdateRequest request) {
        return ApiResponse.<UserSelfResponse>builder()
                .result(userService.updateMyInfo(request))
                .build();
    }

    @PutMapping("/my-info/change-password")
    ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request);
        return ApiResponse.<String>builder().result("User changed password").build();
    }

    @PutMapping("/avatar")
    ApiResponse<UserSelfResponse> updateAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.<UserSelfResponse>builder()
                .result(userService.updateAvatar(file))
                .build();
    }

    @PutMapping("/me")
    ResponseEntity<?> updateSelfUser(@ModelAttribute UserSelfUpdateRequest request) throws IOException {
        userService.updateUserSelf(request);
        return ResponseEntity.ok("User updated");
    }
}
