package org.rliz.cfm.user.boundary;

import org.rliz.cfm.user.model.User;
import org.rliz.cfm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link UserBoundaryService}
 */
@Service
public class UserBoundaryService {

    private UserRepository userRepository;

    @Autowired
    public UserBoundaryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String name) {
        return userRepository.findOneByUsername(name);
    }

}
