package org.rliz.mbs.artist.boundary;

import org.rliz.mbs.artist.model.Artist;

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
}
