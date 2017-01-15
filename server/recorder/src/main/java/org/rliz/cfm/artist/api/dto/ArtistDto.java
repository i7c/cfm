package org.rliz.cfm.artist.api.dto;

import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.api.dto.AbstractDto;
import org.rliz.cfm.common.model.AbstractEntity;

import java.util.UUID;

/**
 * DTO for {@link Artist}s.
 */
public class ArtistDto extends AbstractDto<Artist> {

    public ArtistDto(Artist data) {
        super(data);
    }

    public UUID getMbid() {
        return data.mbid;
    }

    public String getName() {
        return data.name;
    }

}
