package org.rliz.cfm.playback.boundary;

import org.rliz.cfm.playback.api.dto.CreatePlaybackDto;
import org.rliz.cfm.playback.model.Playback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service for business functions related to {@link Playback}s.
 */
public interface PlaybackBoundaryService {

    /**
     * Creates a new playback event using the musicbrainz track ID and release group ID.
     *
     * @param dto the create playback resource
     * @return persisted playback event
     */
    Playback createPlayback(CreatePlaybackDto dto);

    /**
     * Gets a list of {@link Playback}s regardless of the user.
     *
     * @param pageable pageable from the request
     * @return page of playbacks
     */
    Page<Playback> findAll(Pageable pageable);

    /**
     * Gets a page of {@link Playback}s for the current user.
     *
     * @param onlyBroken only include broken playbacks
     * @param pageable   pageable from the request
     * @return page of playbacks
     */
    Page<Playback> findAllForCurrentUser(boolean onlyBroken, Pageable pageable);
}
