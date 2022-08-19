package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.model.User;
import com.learning.timeOffManagement.security.UserPrincipal;

public interface TokenService {
    public String generateToken(User user);
    public UserPrincipal parseToken(String token);
}
