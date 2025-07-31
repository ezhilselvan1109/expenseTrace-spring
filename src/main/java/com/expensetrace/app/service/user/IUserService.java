package com.expensetrace.app.service.user;

import com.expensetrace.app.dto.request.UserRequestDto;
import com.expensetrace.app.dto.response.UserResponseDto;

import java.util.UUID;

public interface IUserService {
    UserResponseDto getUser();
    UserResponseDto addUser(UserRequestDto user);
    UserResponseDto updateUser(UserRequestDto userRequest, UUID id);
    void deleteUserById(UUID id);
}
