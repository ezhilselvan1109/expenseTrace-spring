package com.expensetrace.app.dto.response;

import com.expensetrace.app.enums.DecimalFormatOption;
import com.expensetrace.app.enums.NumberFormatOption;
import com.expensetrace.app.enums.TimeFormat;
import lombok.Data;

@Data
public class SettingsResponseDto {
    private TimeFormat timeFormat; // 12_HOUR or 24_HOUR
    private DecimalFormatOption decimalFormat; // DEFAULT, NO_DECIMALS, ONE_DECIMAL, TWO_DECIMAL
    // Preferences
    private String currencyCode; // e.g., "INR", "USD"
    private NumberFormatOption numberFormat; // MILLIONS, LAKHS, MILLIONS_COMPACT
    // Notifications
    private boolean dailyReminder;
}
