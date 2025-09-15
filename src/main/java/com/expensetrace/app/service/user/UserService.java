package com.expensetrace.app.service.user;

import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.dto.response.UserResponseDto;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    @Override
    public UserResponseDto getUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
    }

    @Override
    public void deleteUserById(UUID id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException("User not found!");
                });
    }
}
