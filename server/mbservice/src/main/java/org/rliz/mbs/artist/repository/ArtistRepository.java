package org.rliz.mbs.artist.repository;

import org.rliz.mbs.artist.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link Artist}s.
 */
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * Retrieves a single artist by its identifier.
     *
     * @param identifier a UUID
     * @return an artist or null if not found
     */
    Artist findOneByIdentifier(UUID identifier);
}
