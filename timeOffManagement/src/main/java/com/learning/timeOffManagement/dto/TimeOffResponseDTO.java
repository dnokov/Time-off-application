package com.learning.timeOffManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeOffResponseDTO implements Serializable {
    private Long id;

    /**
     * Type of timeoff request
     */
    private String type;

    private String reason;

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    /**
     * Name of creator
     */
    private String creator;

    /**
     * Usernames of users who you're awaiting approval from
     */
    private List<String> awaitingApproval;

}
