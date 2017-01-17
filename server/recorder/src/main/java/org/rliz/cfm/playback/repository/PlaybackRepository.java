package org.rliz.cfm.playback.repository;

import org.rliz.cfm.playback.model.Playback;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Playback}s.
 */
public interface PlaybackRepository extends JpaRepository<Playback, Long> {

}
