package org.rliz.mbs.playback.api.dto;

import org.rliz.mbs.artist.api.dto.ArtistDto;
import org.rliz.mbs.artist.model.ArtistCreditName;
import org.rliz.mbs.release.model.ReleaseGroup;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DeepReleaseGroupDto {

    private final ReleaseGroup releaseGroup;

    public DeepReleaseGroupDto(ReleaseGroup releaseGroup) {
        this.releaseGroup = releaseGroup;
    }

    public String getName() {
        return releaseGroup.getName();
    }

    public List<ArtistDto> getArtists() {
        return releaseGroup.getArtistCredit().getArtistCreditName().stream()
                .map(ArtistCreditName::getArtist)
                .map(ArtistDto::new)
                .collect(Collectors.toList());
    }

    public String getComment() {
        return releaseGroup.getComment();
    }

    public Date getLastUpdated() {
        return releaseGroup.getLastUpdated();
    }

    public UUID getIdentifier() {
        return releaseGroup.getIdentifier();
    }
}
