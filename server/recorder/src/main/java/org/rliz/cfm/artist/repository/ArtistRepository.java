package org.rliz.cfm.artist.repository;

import org.rliz.cfm.artist.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
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

    /**
     * Retrieves a list of artists whose mbids are in the given list.
     *
     * @param mbids set of artist mbids
     * @return a list of artists, possible empty
     */
    List<Artist> findByMbidIn(List<UUID> mbids);

}
