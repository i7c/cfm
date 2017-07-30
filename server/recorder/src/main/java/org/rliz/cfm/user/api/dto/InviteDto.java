package org.rliz.cfm.user.api.dto;

import org.rliz.cfm.common.api.dto.AbstractDetachedDto;
import org.rliz.cfm.user.model.Invite;

import java.util.Date;

public class InviteDto extends AbstractDetachedDto {

    public final Date date;

    public InviteDto(Invite invite) {
        super(invite);
        this.date = invite.date;
    }
}
