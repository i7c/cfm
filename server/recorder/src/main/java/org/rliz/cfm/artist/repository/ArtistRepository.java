package org.rliz.cfm.artist.repository;

import org.rliz.cfm.artist.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Artist}s.
 */
public interface ArtistRepository extends JpaRepository<Artist, Long> {

}
