package org.rliz.mbs.release.api;

import org.rliz.mbs.release.api.dto.ReleaseGroupDto;
import org.rliz.mbs.release.api.dto.factory.ReleaseGroupDtoFactory;
import org.rliz.mbs.release.boundary.ReleaseGroupBoundaryService;
import org.rliz.mbs.release.model.ReleaseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST API endpoint for {@link ReleaseGroup}s.
 */
@RestController
@RequestMapping("/api/v1/releasegroups")
public class ReleaseGroupController {

    private ReleaseGroupBoundaryService releaseGroupBoundaryService;

    private ReleaseGroupDtoFactory releaseGroupDtoFactory;

    @Autowired
    public ReleaseGroupController(ReleaseGroupBoundaryService releaseGroupBoundaryService, ReleaseGroupDtoFactory
            releaseGroupDtoFactory) {
        this.releaseGroupBoundaryService = releaseGroupBoundaryService;
        this.releaseGroupDtoFactory = releaseGroupDtoFactory;
    }

    /**
     * Retrieve a {@link ReleaseGroup} by its ID.
     * @param identifier unique identifier for the {@link ReleaseGroup}
     * @return the {@link ReleaseGroup}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{identifier}")
    public ReleaseGroupDto findByIdentifier(@PathVariable("identifier") UUID identifier) {
        ReleaseGroup foundReleaseGroup = releaseGroupBoundaryService.findByIdentifier(identifier);
        return releaseGroupDtoFactory.build(foundReleaseGroup);
    }
}
