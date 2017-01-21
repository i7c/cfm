package org.rliz.cfm.common.security;

import org.rliz.cfm.user.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Methods to ease use of security context.
 */
public final class SecurityContextHelper {

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
