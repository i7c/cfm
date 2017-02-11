package org.rliz.mbs.artist.api.dto;

import org.rliz.mbs.artist.model.Artist;
import org.rliz.mbs.common.api.dto.AbstractDto;

/**
 * DTO for {@link Artist}s.
 */
public class ArtistDto extends AbstractDto<Artist> {

    public ArtistDto(Artist data) {
        super(data);
    }

    public String getName() {
        return data.getName();
    }

    public String getSortName() {
        return data.getSortName();
    }

    public String getComment() {
        return data.getComment();
    }

}
