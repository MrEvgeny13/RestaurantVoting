package ru.topjava.voting.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.topjava.voting.AuthUser;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static AuthUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthUser) ? (AuthUser) principal : null;
    }

    public static AuthUser get() {
        AuthUser user = safeGet();
        requireNonNull(user, "An authorized user is not found");
        return user;
    }

    public static int authUserId() {
        return get().getUserTo().getId();
    }
}