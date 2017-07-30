package org.rliz.cfm.user.boundary;

import org.rliz.cfm.common.exception.EntityNotFoundException;
import org.rliz.cfm.common.exception.IllegalActionException;
import org.rliz.cfm.user.api.dto.RegisterUserDto;
import org.rliz.cfm.user.model.Invite;
import org.rliz.cfm.user.model.User;
import org.rliz.cfm.user.repository.InviteRepository;
import org.rliz.cfm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation for {@link UserBoundaryService}
 */
@Service
public class UserBoundaryService {

    private final UserRepository userRepository;

    private final InviteRepository inviteRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserBoundaryService(UserRepository userRepository, InviteRepository inviteRepository) {
        this.userRepository = userRepository;
        this.inviteRepository = inviteRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User getUserByUsername(String name) {
        return userRepository.findOneByUsername(name);
    }

    @PreAuthorize("@userAuth.hasRegisterPermission(#userIdentifier)")
    public User registerUser(UUID userIdentifier, RegisterUserDto userDetailsDto) {
        User user = userRepository.findOneByIdentifier(userIdentifier);
        if (user == null) {
            throw new EntityNotFoundException(EntityNotFoundException.EC_UNKNOWN_IDENTIFIER, "User not found: {}",
                    userIdentifier);
        }
        Invite invite = inviteRepository.findOneByInvitee(user);
        assert (invite != null);
        if (invite.used) {
            throw new IllegalActionException(IllegalActionException.EC_OPERATION_NOT_PERMITTED,
                    "User is already registered.");
        }
        user.username = userDetailsDto.username;
        user.password = passwordEncoder.encode(userDetailsDto.password);
        user.registered = true;
        invite.used = true;
        inviteRepository.save(invite);
        return userRepository.save(user);
    }

}
