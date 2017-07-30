package org.rliz.cfm.user.api.dto;

import org.rliz.cfm.common.api.dto.AbstractDetachedDto;
import org.rliz.cfm.common.model.AbstractEntity;
import org.rliz.cfm.user.model.User;

public class UserDetailsDto extends AbstractDetachedDto {

    public String username;

    public UserDetailsDto(User user) {
        super(user);
        this.username = user.username;
    }


}
