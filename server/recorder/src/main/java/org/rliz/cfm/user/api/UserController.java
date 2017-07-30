package org.rliz.cfm.user.api;

import org.rliz.cfm.user.api.dto.RegisterUserDto;
import org.rliz.cfm.user.api.dto.UserDetailsDto;
import org.rliz.cfm.user.boundary.UserBoundaryService;
import org.rliz.cfm.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserBoundaryService userBoundaryService;

    @Autowired
    public UserController(UserBoundaryService userBoundaryService) {
        this.userBoundaryService = userBoundaryService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public ResponseEntity<UserDetailsDto> register(@Valid @RequestBody RegisterUserDto registerUserDto,
                                                   @AuthenticationPrincipal(errorOnInvalidType = true) User user) {
        User updatedUser = userBoundaryService.registerUser(user.getIdentifier(), registerUserDto);
        return ResponseEntity.ok(new UserDetailsDto(updatedUser));
    }
}
