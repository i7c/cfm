package org.rliz.cfm.user.boundary;

import org.rliz.cfm.common.security.SecurityContextHelper;
import org.rliz.cfm.user.model.Invite;
import org.rliz.cfm.user.model.User;
import org.rliz.cfm.user.repository.InviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

@Service
public class InviteBoundaryService {

    private final InviteRepository inviteRepository;

    @Autowired
    public InviteBoundaryService(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    public Invite createInvite() {
        User currentUser = SecurityContextHelper.getCurrentUser();
        Invite invite = new Invite(currentUser, Date.from(Instant.now()));
        invite.setIdentifier(UUID.randomUUID());
        return inviteRepository.save(invite);
    }

}
