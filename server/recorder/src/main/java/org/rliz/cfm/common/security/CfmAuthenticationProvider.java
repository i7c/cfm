package org.rliz.cfm.common.security;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rliz.cfm.user.boundary.UserBoundaryService;
import org.rliz.cfm.user.model.Invite;
import org.rliz.cfm.user.model.User;
import org.rliz.cfm.user.repository.InviteRepository;
import org.rliz.cfm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Handles basic auth for cfm.
 */
@Component
public class CfmAuthenticationProvider implements AuthenticationProvider {

    protected final Log logger = LogFactory.getLog(getClass());

    private final UserBoundaryService userBoundaryService;
    private final InviteRepository inviteRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public CfmAuthenticationProvider(UserBoundaryService userBoundaryService, InviteRepository inviteRepository,
                                     UserRepository userRepository) {
        this.userBoundaryService = userBoundaryService;
        this.inviteRepository = inviteRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        if ("$invite$".equals(userName)) {
            UUID inviteId = UUID.fromString(password);
            return inviteLogin(inviteId);
        }
        return regularUserLogin(userName, password);
    }

    private Authentication inviteLogin(UUID inviteId) {
        Invite invite = inviteRepository.findOneByIdentifier(inviteId);
        if (invite == null || invite.used) {
            throw new BadCredentialsException("Invalid invite. Was it already used?");
        }
        User temporaryUser;

        if (invite.invitee != null) {
            temporaryUser = invite.invitee;
        } else {
            temporaryUser = new User("invite-" + inviteId, null);
            temporaryUser.setIdentifier(UUID.randomUUID());
            userRepository.save(temporaryUser);
            invite.invitee = temporaryUser;
            inviteRepository.save(invite);
        }
        return new UsernamePasswordAuthenticationToken(temporaryUser, null, Lists.newArrayList(new
                SimpleGrantedAuthority("ROLE_USER")));
    }

    private Authentication regularUserLogin(String userName, String password) {
        User foundUser = userBoundaryService.getUserByUsername(userName);
        if (foundUser == null) {
            logger.error("Authentication failed because username was not found in database.");
            throw new UsernameNotFoundException("User is not in the database.");
        }
        if (passwordEncoder.matches(password, foundUser.password)) {
            return new UsernamePasswordAuthenticationToken(foundUser, password,
                    Lists.newArrayList(new SimpleGrantedAuthority("ROLE_USER"),
                            new SimpleGrantedAuthority("FULL_PASSWORD")));
        }
        throw new BadCredentialsException("Bad credentials.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
