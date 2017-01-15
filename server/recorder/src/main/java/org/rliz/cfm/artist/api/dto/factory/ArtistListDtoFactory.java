package org.rliz.cfm.artist.api.dto.factory;

import org.rliz.cfm.artist.api.dto.ArtistDto;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.api.dto.ListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory that creates a {@link ListDto} of {@link ArtistDto}s.
 */
@Component
public class ArtistListDtoFactory {

    private ArtistDtoFactory artistDtoFactory;

    @Autowired
    public ArtistListDtoFactory(ArtistDtoFactory artistDtoFactory) {
        this.artistDtoFactory = artistDtoFactory;
    }

    public ListDto<ArtistDto> build(Page<Artist> page) {
        List<ArtistDto> artistDtos = page.getContent().stream()
                .map(artistDtoFactory::build)
                .collect(Collectors.toList());
        return new ListDto<>(artistDtos);
    }

}
