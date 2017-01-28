package org.rliz.cfm.playback.repository;

import org.rliz.cfm.playback.model.Playback;
import org.rliz.cfm.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
    Page<Playback> findByUser(User user, Pageable pageable);

}
