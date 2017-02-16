package org.rliz.mbs.release.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rliz.mbs.artist.model.Artist;
import org.rliz.mbs.artist.model.ArtistCreditName;
import org.rliz.mbs.common.api.dto.AbstractDto;
import org.rliz.mbs.common.api.dto.Reference;
import org.rliz.mbs.release.model.ReleaseGroup;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link ReleaseGroup}s.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReleaseGroupDto extends AbstractDto<ReleaseGroup> {

    public ReleaseGroupDto(ReleaseGroup data) {
        super(data);
    }

    public String getName() {
        return data.getName();
    }

    public List<Reference<Artist>> getArtistReferences() {
        return data.getArtistCredit().getArtistCreditName().stream()
                .map(ArtistCreditName::getArtist)
                .map(Reference::new)
                .collect(Collectors.toList());
    }

    public String getComment() {
        return data.getComment();
    }

    public Date getLastUpdated() {
        return data.getLastUpdated();
    }

}
