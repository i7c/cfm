package org.rliz.cfm.playback.boundary;

import org.rliz.cfm.playback.model.Playback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service for business functions related to {@link Playback}s.
 */
public interface PlaybackBoundaryService {

    /**
     * Creates a new playback event using the musicbrainz track ID and release group ID.
     *
     * @param trackId        the musicbrainz track ID
     * @param releaseGroupId musicbrainz release group ID
     * @param artist         name of the artist
     * @param title          title of the recording
     * @param album          album name
     * @return persisted playback event
     */
    Playback createPlayback(UUID trackId, UUID releaseGroupId, String artist, String title, String album);

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
     * @param pageable pageable from the request
     * @return page of playbacks
     */
    Page<Playback> findAllForCurrentUser(Pageable pageable);
}
