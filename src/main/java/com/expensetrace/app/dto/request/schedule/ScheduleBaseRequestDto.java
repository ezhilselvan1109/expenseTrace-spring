package com.expensetrace.app.dto.request.schedule;

import com.expensetrace.app.enums.schedule.EndType;
import com.expensetrace.app.enums.schedule.FrequencyType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
public class ScheduleBaseRequestDto {
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Time is required")
    private LocalTime startTime;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    private String description;

    @NotNull(message = "Frequency is required")
    private FrequencyType frequencyType;

    private Integer frequencyInterval;

    @NotNull
    private EndType endType;

    private Integer occurrence;

    private Integer remainderDays;

    private Set<String> tags;
}

