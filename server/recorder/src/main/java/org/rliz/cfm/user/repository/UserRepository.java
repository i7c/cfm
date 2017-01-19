package org.rliz.cfm.user.repository;

import org.rliz.cfm.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repo for {@link User}s.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Gets a user by their username.
     *
     * @param username username
     * @return the user if one matches or null otherwise
     */
    User findOneByUsername(String username);

}
