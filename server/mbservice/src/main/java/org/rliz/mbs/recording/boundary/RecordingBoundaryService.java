package org.rliz.mbs.recording.boundary;

import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.release.model.Release;
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
     * @param release release this recording was played from
     * @param title   title of the recording
     * @return the identified recording
     */
    Recording identifyRecording(Release release, String title);

    /**
     * Retrieve single {@link Recording} by identifier.
     *
     * @param identifier unique identifier
     * @return the found {@link Recording}
     */
    Recording findByIdentifier(UUID identifier);
}
