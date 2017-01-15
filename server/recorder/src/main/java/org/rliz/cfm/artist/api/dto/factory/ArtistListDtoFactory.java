package org.rliz.cfm.artist.api.dto.factory;

import org.rliz.cfm.artist.api.dto.ArtistDto;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.api.dto.ListDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory that creates a {@link ListDto} of {@link ArtistDto}s.
 */
@Component
public class ArtistListDtoFactory {

    public ListDto<ArtistDto> build(Page<Artist> page) {
        List<ArtistDto> artistDtos = page.getContent().stream().map(ArtistDto::new).collect(Collectors.toList());
        return new ListDto<>(artistDtos);
    }

}
