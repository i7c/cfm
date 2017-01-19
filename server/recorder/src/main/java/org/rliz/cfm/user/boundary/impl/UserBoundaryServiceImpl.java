package org.rliz.cfm.user.boundary.impl;

import org.rliz.cfm.user.boundary.UserBoundaryService;
import org.rliz.cfm.user.model.User;
import org.rliz.cfm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link UserBoundaryService}
 */
@Service
public class UserBoundaryServiceImpl implements UserBoundaryService {

    private UserRepository userRepository;

    @Autowired
    public UserBoundaryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByUsername(String name) {
        return userRepository.findOneByUsername(name);
    }

}
