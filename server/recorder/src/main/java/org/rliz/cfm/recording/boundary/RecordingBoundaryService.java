package org.rliz.cfm.recording.boundary;

import org.rliz.cfm.recording.model.Recording;

import java.util.UUID;

/**
 * Control Service for {@link Recording}s.
 */
public interface RecordingBoundaryService {

    /**
     * Finds or creates a {@link Recording} by its musicbrainz identifier. If the {@link Recording} is not found in
     * the local database, the musicbrainz API will be queried.
     *
     * @param mbid the musicbrainz identifier
     * @return persisted recording
     */
    Recording findOrCreateRecordingWithMbid(UUID mbid);

}
