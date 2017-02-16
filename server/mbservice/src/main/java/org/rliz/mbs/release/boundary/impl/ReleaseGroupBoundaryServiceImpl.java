package org.rliz.mbs.release.boundary.impl;

import org.rliz.mbs.common.exception.ErrorCodes;
import org.rliz.mbs.common.exception.MbEntityNotFoundException;
import org.rliz.mbs.release.boundary.ReleaseGroupBoundaryService;
import org.rliz.mbs.release.model.ReleaseGroup;
import org.rliz.mbs.release.repository.ReleaseGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation for {@link ReleaseGroupBoundaryService}.
 */
@Service
public class ReleaseGroupBoundaryServiceImpl implements ReleaseGroupBoundaryService {

    private ReleaseGroupRepository releaseGroupRepository;

    @Autowired
    public ReleaseGroupBoundaryServiceImpl(ReleaseGroupRepository releaseGroupRepository) {
        this.releaseGroupRepository = releaseGroupRepository;
    }

    @Override
    public ReleaseGroup findByIdentifier(UUID identifier) {
        ReleaseGroup foundReleaseGroup = releaseGroupRepository.findByIdentifier(identifier);
        if (foundReleaseGroup == null) {
            throw new MbEntityNotFoundException(String.format("No release group for identifier %s.", identifier),
                    ErrorCodes.EC_001);
        }
        return foundReleaseGroup;
    }
}
