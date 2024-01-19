package com.soloproject.shoppingmall.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtils {

    public static String getAuthUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return username;

        } else {
            String username = principal.toString();
            return username;
        }
    }
}
