package com.learning.timeOffManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponseDTO {

    @NotEmpty
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;
}
