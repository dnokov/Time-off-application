package academy.scalefocus.timeOffManagement.utils;

import academy.scalefocus.timeOffManagement.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserPrincipalUtils {

    public static UserPrincipal getPrincipalFromContext(){
        return (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
