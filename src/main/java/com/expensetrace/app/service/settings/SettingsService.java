package com.expensetrace.app.service.settings;

import com.expensetrace.app.enums.DecimalFormatOption;
import com.expensetrace.app.enums.NumberFormatOption;
import com.expensetrace.app.enums.TimeFormat;
import com.expensetrace.app.model.Settings;
import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.SettingRepository;
import com.expensetrace.app.responseDto.SettingsResponseDto;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SettingsService implements ISettingsService {

    private final SettingRepository settingsRepository;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    @Override
    public SettingsResponseDto getSettings() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        User user=new User();
        user.setId(userId);
        Settings setting = settingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
        return modelMapper.map(setting, SettingsResponseDto.class);
    }

    @Override
    public void addSettings(User user) {
        Settings settings=new Settings();
        settings.setUser(user);
        settings.setTimeFormat(TimeFormat.TWELVE_HOUR);
        settings.setDecimalFormat(DecimalFormatOption.ONE_DECIMAL);
        settings.setCurrencyCode("91");
        settings.setNumberFormat(NumberFormatOption.LAKHS);
        settings.setDailyReminder(true);
        settingsRepository.save(settings);
    }

    @Override
    public void updateTimeFormat(int formatCode) {
        Settings settings = getCurrentUserSettings();
        settings.setTimeFormat(TimeFormat.fromCode(formatCode));
        settingsRepository.save(settings);
    }

    @Override
    public void updateCurrencyCode(String currencyCode) {
        Settings settings = getCurrentUserSettings();
        settings.setCurrencyCode(currencyCode);
        settingsRepository.save(settings);
    }

    @Override
    public void updateNumberFormat(int formatCode) {
        Settings settings = getCurrentUserSettings();
        settings.setNumberFormat(NumberFormatOption.fromCode(formatCode));
        settingsRepository.save(settings);
    }

    @Override
    public void updateDailyReminder(boolean dailyReminder) {
        Settings settings = getCurrentUserSettings();
        settings.setDailyReminder(dailyReminder);
        settingsRepository.save(settings);
    }

    private Settings getCurrentUserSettings() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        User user = new User();
        user.setId(userId);
        return settingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
    }

}
