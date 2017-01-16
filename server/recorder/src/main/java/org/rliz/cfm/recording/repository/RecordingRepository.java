package org.rliz.cfm.recording.repository;

import org.rliz.cfm.recording.model.Recording;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link Recording}s.
 */
public interface RecordingRepository extends JpaRepository<Recording, Long> {

    /**
     * Retrieves a {@link Recording} by its musicbrainz identifier.
     *
     * @param mbid the musicbrainz record identifier
     * @return the found recording or null
     */
    Recording findOneByMbid(UUID mbid);

}
