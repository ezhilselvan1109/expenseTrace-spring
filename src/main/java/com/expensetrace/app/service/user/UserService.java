package com.expensetrace.app.service.user;

import com.expensetrace.app.requestDto.UserRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.exception.UsernameNotFoundException;
import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.responseDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new AlreadyExistsException(userRequestDto.getEmail() + " already exists");
        }

        User user = modelMapper.map(userRequestDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }


    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto, Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        existingUser.setFirstName(userRequestDto.getFirstName());
        existingUser.setLastName(userRequestDto.getLastName());
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setPassword(userRequestDto.getPassword());

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserResponseDto.class);
    }



    @Override
    public void deleteUserById(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }
}
