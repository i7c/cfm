package org.rliz.cfm.common.security;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rliz.cfm.user.boundary.UserBoundaryService;
import org.rliz.cfm.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Handles basic auth for cfm.
 */
@Component
public class CfmAuthenticationProvider implements AuthenticationProvider {

    protected final Log logger = LogFactory.getLog(getClass());

    private UserBoundaryService userBoundaryService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CfmAuthenticationProvider(UserBoundaryService userBoundaryService) {
        this.userBoundaryService = userBoundaryService;
        this.passwordEncoder = NoOpPasswordEncoder.getInstance();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

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
