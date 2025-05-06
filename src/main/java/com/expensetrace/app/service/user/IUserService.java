package com.expensetrace.app.service.user;

import com.expensetrace.app.requestDto.UserRequestDto;
import com.expensetrace.app.responseDto.UserResponseDto;

import java.util.List;

public interface IUserService {
    UserResponseDto getUserById(Long id);
    UserResponseDto getUserByName(String name);
    List<UserResponseDto> getAllUsers();
    UserResponseDto addUser(UserRequestDto user);
    UserResponseDto updateUser(UserRequestDto userRequest, Long id);
    void deleteUserById(Long id);
}
