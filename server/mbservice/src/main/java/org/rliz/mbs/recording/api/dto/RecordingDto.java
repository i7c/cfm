package org.rliz.mbs.recording.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rliz.mbs.artist.model.Artist;
import org.rliz.mbs.artist.model.ArtistCreditName;
import org.rliz.mbs.common.api.dto.AbstractDto;
import org.rliz.mbs.common.api.dto.Reference;
import org.rliz.mbs.recording.model.Recording;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link Recording}s.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordingDto extends AbstractDto<Recording> {

    public RecordingDto(Recording data) {
        super(data);
    }

    public String getName() {
        return data.getName();
    }

    public Long getLength() {
        return data.getLength();
    }

    public String getComment() {
        return data.getComment();
    }

    public Date getLastUpdated() {
        return data.getLastUpdated();
    }

    public Boolean getVideo() {
        return data.getVideo();
    }

    public List<Reference<Artist>> getArtistReferences() {
        return data.getArtistCredit().getArtistCreditName().stream()
                .map(ArtistCreditName::getArtist)
                .map(Reference::new)
                .collect(Collectors.toList());
    }

}
