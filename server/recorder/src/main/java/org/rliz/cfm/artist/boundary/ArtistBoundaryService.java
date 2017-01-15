package org.rliz.cfm.artist.boundary;

import org.rliz.cfm.artist.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Boundary service for {@link org.rliz.cfm.artist.model.Artist}s.
 */
public interface ArtistBoundaryService {

    Page<Artist> findAllArtists(Pageable pageable);
}
