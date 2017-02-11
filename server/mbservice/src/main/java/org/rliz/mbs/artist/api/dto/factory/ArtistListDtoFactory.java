package org.rliz.mbs.artist.api.dto.factory;

import org.rliz.mbs.artist.api.dto.ArtistDto;
import org.rliz.mbs.artist.model.Artist;
import org.rliz.mbs.common.api.dto.ListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cmw on 11/02/17.
 */
@Component
public class ArtistListDtoFactory {

    private ArtistDtoFactory artistDtoFactory;

    @Autowired
    public ArtistListDtoFactory(ArtistDtoFactory artistDtoFactory) {
        this.artistDtoFactory = artistDtoFactory;
    }

    public ListDto<ArtistDto> build(Page<Artist> artists) {
        List<ArtistDto> artistDtos = artists.getContent().stream()
                .map(artistDtoFactory::build).collect(Collectors.toList());
        return new ListDto<>(artistDtos, artists);
    }
}
