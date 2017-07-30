package org.rliz.cfm.user.api.dto;

import javax.validation.constraints.NotNull;

public class RegisterUserDto {

    @NotNull
    public String username;

    @NotNull
    public String password;

}
