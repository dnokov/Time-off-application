package com.learning.timeOffManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreationDTO {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
}
