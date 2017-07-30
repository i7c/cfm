package org.rliz.cfm.user.repository;

import org.rliz.cfm.user.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, Long> {

}
