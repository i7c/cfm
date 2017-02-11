package org.rliz.mbs.artist.api.dto.factory;

import org.rliz.mbs.artist.api.dto.ArtistDto;
import org.rliz.mbs.artist.model.Artist;
import org.springframework.stereotype.Component;

/**
 * Factory to build {@link ArtistDto}s.
 */
@Component
public class ArtistDtoFactory {

    public ArtistDto build(Artist artist) {
        return new ArtistDto(artist);
    }

}
