package org.rliz.cfm.playback.repository;

import org.rliz.cfm.playback.model.Playback;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link Playback}s.
 */
public interface PlaybackRepository extends JpaRepository<Playback, Long> {

    /**
     * Finds playbacks for a user.
     *
     * @param user     the user
     * @param pageable pageable from the request
     * @return a page of playbacks for this user
     */
    @EntityGraph(attributePaths = {"recording.artists", "releaseGroup", "originalArtists", "user" })
    Page<Playback> findByUser(User user, Pageable pageable);


    /**
     * Finds all playbacks of given user that refer to the specified recording.
     *
     * @param user      a user
     * @param recording a recording
     * @param pageable  page request
     * @return the playbacks
     */
    Page<Playback> findByUserAndRecording(User user, Recording recording, Pageable pageable);

    /**
     * Finds a single playback by its identifier.
     *
     * @param identifier the uuid
     * @return the {@link Playback}
     */
    Playback findOneByIdentifier(UUID identifier);
}
