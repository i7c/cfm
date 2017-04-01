package org.rliz.mbs.recording.boundary;

import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.release.model.Release;
import org.rliz.mbs.release.model.ReleaseGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service for {@link Recording}s.
 */
public interface RecordingBoundaryService {

    /**
     * Tries to identify a {@link Recording} using given release the recording appears on and title of the recording.
     *
     * @param release   release this recording was played from
     * @param title     title of the recording
     * @param threshold min score to consider the result a match
     * @return the identified recording
     */
    Recording identifyRecording(Release release, String title, int threshold);

    /**
     * Retrieve single {@link Recording} by identifier.
     *
     * @param identifier unique identifier
     * @return the found {@link Recording}
     */
    Recording findByIdentifier(UUID identifier);

    /**
     * Lookup a recording with given {@link ReleaseGroup} on which the recording appears and the name of the recording.
     *
     * @param releaseGroupId the identifier of the release group
     * @param name           the name of the recording
     * @param pageable       page request
     * @return list of recordings ordered by rating
     */
    Page<Recording> findRecordingByReleaseGroupIdentifierAndName(UUID releaseGroupId, String name, Pageable pageable);
}
