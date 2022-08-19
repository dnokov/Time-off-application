package com.learning.timeOffManagement.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginRequestDTO {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
