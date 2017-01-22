package org.rliz.cfm.recording.api.dto;

import org.rliz.cfm.artist.api.dto.ArtistDto;
import org.rliz.cfm.common.api.dto.AbstractDto;
import org.rliz.cfm.common.api.dto.ListDto;
import org.rliz.cfm.recording.model.Recording;

import java.util.List;
import java.util.UUID;

/**
 * Represents a {@link Recording} on the wire.
 */
public class RecordingDto extends AbstractDto<Recording> {

    private List<ArtistDto> artists;

    public RecordingDto(Recording data, List<ArtistDto> artistListDto) {
        super(data);
        this.artists = artistListDto;
    }

    public UUID getMbid() {
        return data.getMbid();
    }

    public String getTitle() {
        return data.getTitle();
    }

    public List<ArtistDto> getArtists() {
        return artists;
    }
}
