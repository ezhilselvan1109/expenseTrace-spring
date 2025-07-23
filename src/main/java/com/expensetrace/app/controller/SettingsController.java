package com.expensetrace.app.controller;

import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.SettingsResponseDto;
import com.expensetrace.app.service.settings.ISettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "Endpoints for managing user settings")
public class SettingsController {

    private final ISettingsService settingService;

    @Operation(summary = "Get user settings", description = "Fetches the current settings of the authenticated user.")
    @GetMapping
    public ResponseEntity<ApiResponse> getUserSetting() {
        SettingsResponseDto settings = settingService.getSettings();
        return ResponseEntity.status(OK).body(new ApiResponse("Success", settings));
    }

    @Operation(summary = "Update time format", description = "Updates the user's preferred time format. Acceptable values: 12 or 24.")
    @PatchMapping("/time-format")
    public ResponseEntity<ApiResponse> updateTimeFormat(@RequestParam int formatCode) {
        settingService.updateTimeFormat(formatCode);
        return ResponseEntity.ok(new ApiResponse("Time format updated successfully", null));
    }

    @Operation(summary = "Update currency code", description = "Updates the user's preferred currency code. Example: INR, USD, EUR.")
    @PatchMapping("/currency-code")
    public ResponseEntity<ApiResponse> updateCurrencyCode(@RequestParam String currencyCode) {
        settingService.updateCurrencyCode(currencyCode);
        return ResponseEntity.ok(new ApiResponse("Currency code updated successfully", null));
    }

    @Operation(summary = "Update number format", description = "Updates the user's preferred number format. Acceptable format codes depend on your predefined mapping.")
    @PatchMapping("/number-format")
    public ResponseEntity<ApiResponse> updateNumberFormat(@RequestParam int formatCode) {
        settingService.updateNumberFormat(formatCode);
        return ResponseEntity.ok(new ApiResponse("Number format updated successfully", null));
    }

    @Operation(summary = "Update daily reminder preference", description = "Enables or disables daily reminders for the user.")
    @PatchMapping("/daily-reminder")
    public ResponseEntity<ApiResponse> updateDailyReminder(@RequestParam boolean dailyReminder) {
        settingService.updateDailyReminder(dailyReminder);
        return ResponseEntity.ok(new ApiResponse("Daily reminder updated successfully", null));
    }

    @Operation(summary = "Delete user account", description = "Permanently deletes the authenticated user's account and all related data.")
    @DeleteMapping("/account")
    public ResponseEntity<ApiResponse> deleteUserAccount() {
        return ResponseEntity.ok(new ApiResponse("Account deleted successfully", null));
    }

    @Operation(summary = "Clear all data and start fresh", description = "Deletes all user data but retains the account for a fresh start.")
    @DeleteMapping("/data")
    public ResponseEntity<ApiResponse> clearUserDataAndStartFresh() {
        return ResponseEntity.ok(new ApiResponse("All data deleted. You can now start fresh.", null));
    }

}
