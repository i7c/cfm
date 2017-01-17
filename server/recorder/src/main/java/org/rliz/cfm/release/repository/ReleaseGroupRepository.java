package org.rliz.cfm.release.repository;

import org.rliz.cfm.release.model.ReleaseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link ReleaseGroup}s.
 */
public interface ReleaseGroupRepository extends JpaRepository<ReleaseGroup, Long> {

    /**
     * Returns a single {@link ReleaseGroup} identified by musicbrainz ID.
     *
     * @param mbid the musicbrainz ID
     * @return a release group or null if none was found
     */
    ReleaseGroup findOneByMbid(UUID mbid);

}
