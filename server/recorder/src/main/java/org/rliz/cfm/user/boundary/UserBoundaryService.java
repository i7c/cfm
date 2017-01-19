package org.rliz.cfm.user.boundary;

import org.rliz.cfm.user.model.User;

/**
 * Service for cfm {@link User}s.
 */
public interface UserBoundaryService {

    /**
     * Gets a user by their name
     *
     * @param name username
     * @return user or null if not found
     */
    User getUserByUsername(String name);

}
