package org.rliz.mbs.artist.repository;

import org.rliz.mbs.artist.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
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
    @EntityGraph(attributePaths = {"artistType", "area"})
    Artist findOneByIdentifier(UUID identifier);

    /**
     * Find artist by name.
     *
     * @param name     artist name
     * @param pageable page request
     * @return page of artists
     */
    @EntityGraph(attributePaths = {"artistType", "area"})
    Page<Artist> findByName(String name, Pageable pageable);
}
