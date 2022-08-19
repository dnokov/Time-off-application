package com.learning.timeOffManagement.utils;

import com.learning.timeOffManagement.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserPrincipalUtils {

    public static UserPrincipal getPrincipalFromContext(){
        return (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
