package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.model.User;
import academy.scalefocus.timeOffManagement.security.UserPrincipal;

public interface TokenService {
    public String generateToken(User user);
    public UserPrincipal parseToken(String token);
}
