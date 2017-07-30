package org.rliz.cfm.user.repository;

import org.rliz.cfm.user.model.Invite;
import org.rliz.cfm.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, Long> {


    Invite findOneByIdentifier(UUID identifier);

    Invite findOneByInvitee(User invitee);

}
