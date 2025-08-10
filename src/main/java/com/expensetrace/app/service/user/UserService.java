package com.expensetrace.app.service.user;

import com.expensetrace.app.dto.request.UserRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.dto.response.UserResponseDto;
import com.expensetrace.app.service.EmailService;
import com.expensetrace.app.service.account.IAccountService;
import com.expensetrace.app.service.category.ICategoryService;
import com.expensetrace.app.service.settings.ISettingsService;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final IAccountService accountService;
    private final SecurityUtil securityUtil;
    private final ICategoryService categoryService;
    private final ISettingsService settingService;
    private final EmailService emailService;
    @Override
    public UserResponseDto getUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new AlreadyExistsException(userRequestDto.getEmail() + " already exists");
        }

        User user = modelMapper.map(userRequestDto, User.class);
        user.setEnabled(false); // not verified yet

        // Generate token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);

        User savedUser = userRepository.save(user);

        accountService.addCashAccount(savedUser);
        settingService.addSettings(savedUser);
        categoryService.createDefaultCategoriesForUser(savedUser);

        // Send mail with token
        emailService.sendActivationEmail(userRequestDto.getEmail(),user.getFirstName(), token);

        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
        existingUser.setFirstName(userRequestDto.getFirstName());
        existingUser.setLastName(userRequestDto.getLastName());
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setPassword(userRequestDto.getPassword());

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserResponseDto.class);
    }

    @Override
    public void deleteUserById(UUID id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException("User not found!");
                });
    }

    public boolean verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
        user.setEnabled(true);
        user.setVerificationToken(null); // remove token after verification
        userRepository.save(user);
        return true;
    }

    public void resetPassword(String newPassword) {
        UUID userId = securityUtil.getResetUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public boolean verifyOtp(String otp) {
        UUID userId = securityUtil.getResetUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));

        if (user.getOtpCode() == null || user.getOtpExpiry() == null) {
            throw new IllegalStateException("OTP not generated");
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            throw new IllegalStateException("OTP expired");
        }

        if (!user.getOtpCode().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        // OTP verified successfully → clear it
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        return true;
    }

    public void sendForgotPasswordOtp(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        String otp = generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), user.getFirstName(), otp);
    }

    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

}
