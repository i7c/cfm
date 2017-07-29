package org.rliz.cfm.common.lifecycle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rliz.cfm.user.model.User;
import org.rliz.cfm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Adds an root {@link User} if specified as argument.
 */
@Component
public class CfmRootUserInitialiser implements SmartLifecycle {

    protected final Log logger = LogFactory.getLog(getClass());

    private final UserRepository userRepository;

    @Value("${cfm.root_user:}")
    private String rootUser;

    @Value("${cfm.root_password:}")
    private String rootPassword;

    private boolean running;

    public CfmRootUserInitialiser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        logger.info("Stopping " + this.getClass().getSimpleName());
        running = false;
        runnable.run();
    }

    @Override
    public void start() {
        logger.info("Starting " + this.getClass().getSimpleName());
        addRootUser();
        running = true;
    }

    @Override
    public void stop() {
        logger.info("Stopping " + this.getClass().getSimpleName());
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return LifecyclePhases.ROOT_USER_PHASE;
    }

    @Transactional
    private void addRootUser() {
        User user = userRepository.findOneByUsername(rootUser);
        if (user == null) {
            if (StringUtils.isNotEmpty(rootUser)) {
                logger.info("Adding root user, because it was not found.");
                user = new User(rootUser, rootPassword);
                user.setIdentifier(UUID.randomUUID());
                userRepository.save(user);
            } else {
                logger.warn("There is no root user and none was specified for initialisation.");
            }
        }
    }
}
