package org.rliz.cfm.artist.repository;

import org.rliz.cfm.artist.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link Artist}s.
 */
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * Retrieves a single {@link Artist} by its identifier
     *
     * @param identifier the identifier of the artist
     * @return the artist or null if not found
     */
    Artist findOneByIdentifier(UUID identifier);

}
