package com.expensetrace.app.dto.response.schedule;

import com.expensetrace.app.dto.response.tag.TagResponseDto;
import com.expensetrace.app.enums.schedule.EndType;
import com.expensetrace.app.enums.schedule.ExecutionStatus;
import com.expensetrace.app.enums.schedule.FrequencyType;
import com.expensetrace.app.enums.schedule.ScheduleType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
public class ScheduleBaseResponseDto {

    private UUID id;

    private LocalDate startDate;

    private LocalTime time;

    private BigDecimal amount;

    private String description;

    private ScheduleType type;

    private FrequencyType frequencyType;

    private Integer frequencyInterval;

    private EndType endType;

    private Integer occurrence;

    private Integer remainderDays;

    private ExecutionStatus status;

    private Set<TagResponseDto> tags;
}

