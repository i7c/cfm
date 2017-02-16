package org.rliz.mbs.release.boundary;

import org.rliz.mbs.release.model.ReleaseGroup;

import java.util.UUID;

/**
 * Service for {@link ReleaseGroup}s.
 */
public interface ReleaseGroupBoundaryService {

    /**
     * Retrieve single {@link ReleaseGroup}
     * @param identifier uuid of the {@link ReleaseGroup}
     * @return the {@link ReleaseGroup}
     */
    ReleaseGroup findByIdentifier(UUID identifier);
}
