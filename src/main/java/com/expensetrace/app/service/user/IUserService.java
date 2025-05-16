package com.expensetrace.app.service.user;

import com.expensetrace.app.requestDto.UserRequestDto;
import com.expensetrace.app.responseDto.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    UserResponseDto getUserById(UUID id);
    UserResponseDto addUser(UserRequestDto user);
    UserResponseDto updateUser(UserRequestDto userRequest, UUID id);
    void deleteUserById(UUID id);
}
