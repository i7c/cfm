package org.rliz.cfm.user.api;

import org.rliz.cfm.user.api.dto.InviteDto;
import org.rliz.cfm.user.boundary.InviteBoundaryService;
import org.rliz.cfm.user.model.Invite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/v1/invites")
public class InviteController {

    private InviteBoundaryService inviteBoundaryService;

    @Autowired
    public InviteController(InviteBoundaryService inviteBoundaryService) {
        this.inviteBoundaryService = inviteBoundaryService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<InviteDto> createInvite() {
        Invite invite = inviteBoundaryService.createInvite();
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentServletMapping().path
                ("/api/v1/invite/{inviteId}").buildAndExpand(invite.getIdentifier()).toUri()
        ).body(new InviteDto(invite));
    }
}
