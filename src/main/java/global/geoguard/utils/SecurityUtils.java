package global.geoguard.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import global.geoguard.model.User;

public class SecurityUtils {
    public static User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
