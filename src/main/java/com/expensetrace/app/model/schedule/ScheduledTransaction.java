package com.expensetrace.app.model.schedule;

import com.expensetrace.app.enums.schedule.EndType;
import com.expensetrace.app.enums.schedule.ExecutionStatus;
import com.expensetrace.app.enums.schedule.FrequencyType;
import com.expensetrace.app.enums.schedule.ScheduleType;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "scheduled_transaction")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class ScheduledTransaction {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ScheduleType type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    private Integer frequencyInterval;
    @Enumerated(EnumType.STRING)
    private EndType endType;
    private Integer occurrence;
    private Integer reminderDays;
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "scheduled_transaction_tags",
            joinColumns = @JoinColumn(name = "scheduled_transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "scheduledTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ScheduledTransactionOccurrence> occurrencesList = new HashSet<>();
}