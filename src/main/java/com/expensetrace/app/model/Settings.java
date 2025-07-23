package com.expensetrace.app.model;

import com.expensetrace.app.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_settings")
public class Settings {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Appearance
    @Enumerated(EnumType.STRING)
    private TimeFormat timeFormat; // 12_HOUR or 24_HOUR

    @Enumerated(EnumType.STRING)
    private DecimalFormatOption decimalFormat; // DEFAULT, NO_DECIMALS, ONE_DECIMAL, TWO_DECIMAL

    // Preferences
    private String currencyCode; // e.g., "INR", "USD"

    @Enumerated(EnumType.STRING)
    private NumberFormatOption numberFormat; // MILLIONS, LAKHS, MILLIONS_COMPACT

    // Notifications
    private boolean dailyReminder;
}
