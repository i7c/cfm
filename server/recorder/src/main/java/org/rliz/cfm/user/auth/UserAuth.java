package org.rliz.cfm.user.auth;

import org.rliz.cfm.common.security.SecurityContextHelper;
import org.rliz.cfm.user.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserAuth {

    public boolean hasRegisterPermission(UUID userIdentifier) {
        User user = SecurityContextHelper.getCurrentUser();

        if (!user.getIdentifier().equals(userIdentifier)) return false;
        return !user.registered;
    }
}
