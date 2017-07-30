package org.rliz.cfm.user.repository;

import org.rliz.cfm.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repo for {@link User}s.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByUsername(String username);

    User findOneByIdentifier(UUID identifier);

}
