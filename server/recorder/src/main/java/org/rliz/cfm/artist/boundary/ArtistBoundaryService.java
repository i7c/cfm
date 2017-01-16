package org.rliz.cfm.artist.boundary;

import org.rliz.cfm.artist.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Boundary service for {@link org.rliz.cfm.artist.model.Artist}s.
 */
public interface ArtistBoundaryService {

    /**
     * Finds all artists
     *
     * @param pageable the pageable from the request
     * @return a page of {@link Artist}s
     */
    Page<Artist> findAllArtists(Pageable pageable);

    /**
     * Gets one {@link Artist} by UUID.
     * @param identifier the uuid of the artist
     * @return the artist or null if not found
     */
    Artist findOneByIdentifier(UUID identifier);

    /**
     * Creates a new artist in the database
     *
     * @param name the name of the artist
     * @param mbid the musicbrainz uuid
     * @return the newly created artist
     */
    Artist createArtist(String name, UUID mbid);

    /**
     * Gets or creates a list of artists using the Musicbrainz ID.
     * @param mbids
     * @return
     */
    List<Artist> getOrCreateArtistsWithMusicbrainz(List<UUID> mbids);
}
