package com.learning.timeOffManagement.security;

import lombok.Data;
import lombok.NonNull;

import javax.security.auth.Subject;
import java.security.Principal;

@Data
public class UserPrincipal implements Principal {
    @NonNull
    private Long id;
    @NonNull
    private String userName;
    @NonNull
    private boolean isAdmin;

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}