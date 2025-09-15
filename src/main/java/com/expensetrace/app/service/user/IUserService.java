package com.expensetrace.app.service.user;

import com.expensetrace.app.dto.response.UserResponseDto;
import com.expensetrace.app.model.User;

import java.util.UUID;

public interface IUserService {
    UserResponseDto getUser();
    void deleteUserById(UUID id);
    User getAuthenticatedUser();
}
