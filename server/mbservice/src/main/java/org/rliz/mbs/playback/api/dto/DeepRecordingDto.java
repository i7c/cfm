package org.rliz.mbs.playback.api.dto;

import org.rliz.mbs.artist.api.dto.ArtistDto;
import org.rliz.mbs.artist.model.ArtistCreditName;
import org.rliz.mbs.recording.model.Recording;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DeepRecordingDto {

    private Recording recording;

    public DeepRecordingDto(Recording recording) {
        this.recording = recording;
    }

    public String getName() {
        return recording.getName();
    }

    public Long getLength() {
        return recording.getLength();
    }

    public String getComment() {
        return recording.getComment();
    }

    public Date getLastUpdated() {
        return recording.getLastUpdated();
    }

    public UUID getIdentifier() {
        return recording.getIdentifier();
    }

    public List<ArtistDto> getArtists() {
        return recording.getArtistCredit().getArtistCreditName().stream()
                .map(ArtistCreditName::getArtist)
                .map(ArtistDto::new)
                .collect(Collectors.toList());
    }
}
