package org.rliz.mbs.artist.boundary;

import org.rliz.mbs.artist.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Boundary for {@link Artist}s.
 */
public interface ArtistBoundaryService {

    /**
     * Gets a single {@link Artist} from the repository.
     *
     * @param identifier the artist's UUID
     * @return the artist
     */
    Artist getSingleArtistByIdentifier(UUID identifier);

    /**
     * Finds artists by their name.
     *
     * @param name     artist name
     * @param pageable page request
     * @return page of artists
     */
    Page<Artist> findArtistsByName(String name, Pageable pageable);
}
