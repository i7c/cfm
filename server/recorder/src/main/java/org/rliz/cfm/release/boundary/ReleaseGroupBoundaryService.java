package org.rliz.cfm.release.boundary;

import org.rliz.cfm.release.model.ReleaseGroup;

import java.util.UUID;

/**
 * Service for {@link ReleaseGroup}s.
 */
public interface ReleaseGroupBoundaryService {

    /**
     * Retrieves a {@link ReleaseGroup} from the database or queries the musicbrainz db for it and persists the
     * received data.
     *
     * @param mbid the musicbrainz ID of the release group
     * @return the found or created release group
     */
    ReleaseGroup findOrCreateReleaseGroupWithMusicbrainz(UUID mbid);

}
