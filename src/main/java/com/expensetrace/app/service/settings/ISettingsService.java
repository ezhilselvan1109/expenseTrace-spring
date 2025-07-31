package com.expensetrace.app.service.settings;

import com.expensetrace.app.model.User;
import com.expensetrace.app.dto.response.SettingsResponseDto;

public interface ISettingsService {
    SettingsResponseDto getSettings();
    void addSettings(User user);
    void updateTimeFormat(int formatCode);
    void updateCurrencyCode(String currencyCode);
    void updateNumberFormat(int formatCode);
    void updateDailyReminder(boolean dailyReminder);

}
