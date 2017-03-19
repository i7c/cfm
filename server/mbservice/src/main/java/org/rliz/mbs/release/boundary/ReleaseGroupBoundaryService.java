package org.rliz.mbs.release.boundary;

import org.rliz.mbs.release.model.ReleaseGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service for {@link ReleaseGroup}s.
 */
public interface ReleaseGroupBoundaryService {

    /**
     * Retrieve single {@link ReleaseGroup}
     *
     * @param identifier uuid of the {@link ReleaseGroup}
     * @return the {@link ReleaseGroup}
     */
    ReleaseGroup findByIdentifier(UUID identifier);

    /**
     * Find release groups ordered by rating (best matching first) using the credited artists and the name of the
     * release group.
     *
     * @param artists  credited artists for this release
     * @param name     name of the release
     * @param pageable page request
     * @return a slice of the ordered result
     */
    Page<ReleaseGroup> findByArtistsAndName(List<String> artists, String name, Pageable pageable);
}
