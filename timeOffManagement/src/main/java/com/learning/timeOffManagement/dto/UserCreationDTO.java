package com.learning.timeOffManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDTO {
    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9]*", message = "Username can have only letters and digits")
    @Size(min = 1, max = 20, message = "Username should be between 2 and 20 characters")
    private String username;

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]*", message = "First name can have only letters")
    @Size(min = 1, max = 20, message = "Username should be between 2 and 20 characters")
    private String firstName;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]*", message = "Last name can have only letters")
    @Size(min = 1, max = 20, message = "Username should be between 2 and 20 characters")
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    private Boolean isAdmin;
}
