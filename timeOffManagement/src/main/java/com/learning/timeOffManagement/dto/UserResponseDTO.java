package com.learning.timeOffManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    @NotEmpty
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotNull
    private LocalDate dateOfCreation;
    @NotNull
    private LocalDate dateOfLastChange;
    @NotNull
    private Long editedBy;
    @NotNull
    private Boolean isAdmin;
    private int paidLeave;
    private int unpaidLeave;
    private int sickLeave;
    @NotEmpty
    private String email;

    private boolean inOffice;
    /**
     * The id and the title of the teams, in which the user is part of
     */
    private List<String> teamsInformation;
    private boolean employed;


}
