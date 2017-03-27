package org.rliz.mbs.release.api;

import org.rliz.mbs.common.api.dto.ListDto;
import org.rliz.mbs.release.api.dto.ReleaseGroupDto;
import org.rliz.mbs.release.api.dto.factory.ReleaseGroupDtoFactory;
import org.rliz.mbs.release.api.dto.factory.ReleaseGroupListDtoFactory;
import org.rliz.mbs.release.boundary.ReleaseGroupBoundaryService;
import org.rliz.mbs.release.model.ReleaseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST API endpoint for {@link ReleaseGroup}s.
 */
@RestController
@RequestMapping("/mbs/v1/releasegroups")
public class ReleaseGroupController {

    private ReleaseGroupBoundaryService releaseGroupBoundaryService;

    private ReleaseGroupDtoFactory releaseGroupDtoFactory;

    private ReleaseGroupListDtoFactory releaseGroupListDtoFactory;

    @Autowired
    public ReleaseGroupController(ReleaseGroupBoundaryService releaseGroupBoundaryService,
                                  ReleaseGroupDtoFactory releaseGroupDtoFactory,
                                  ReleaseGroupListDtoFactory releaseGroupListDtoFactory) {
        this.releaseGroupBoundaryService = releaseGroupBoundaryService;
        this.releaseGroupDtoFactory = releaseGroupDtoFactory;
        this.releaseGroupListDtoFactory = releaseGroupListDtoFactory;
    }

    /**
     * Retrieve a {@link ReleaseGroup} by its ID.
     *
     * @param identifier unique identifier for the {@link ReleaseGroup}
     * @return the {@link ReleaseGroup}
     */
    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "/{identifier}")
    public ReleaseGroupDto findByIdentifier(@PathVariable("identifier") UUID identifier) {
        ReleaseGroup foundReleaseGroup = releaseGroupBoundaryService.findByIdentifier(identifier);
        return releaseGroupDtoFactory.build(foundReleaseGroup);
    }

    /**
     * Find a release group by artists and name. The paged result is sorted, best matching first.
     *
     * @param artists  names of credited artists for the release
     * @param name     name of the release
     * @param pageable page request
     * @return list of found release groups, ordered by rating
     */
    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET)
    public ListDto<ReleaseGroupDto> findByArtistsAndName(@RequestParam("artist") List<String> artists,
                                                         @RequestParam("release") String name,
                                                         Pageable pageable) {
        Page<ReleaseGroup> foundReleaseGroups = releaseGroupBoundaryService.findByArtistsAndName(artists, name,
                pageable);
        return releaseGroupListDtoFactory.build(foundReleaseGroups);
    }
}
