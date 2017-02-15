package org.rliz.mbs.playback.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rliz.mbs.artist.model.Artist;
import org.rliz.mbs.artist.model.ArtistCreditName;
import org.rliz.mbs.common.api.dto.Reference;
import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.release.model.Release;
import org.rliz.mbs.release.model.ReleaseGroup;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by cmw on 15/02/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaybackDetailsDto {

    private Release release;

    private Recording recording;

    /**
     * Constructor.
     *
     * @param release   release of the described playback
     * @param recording recording of the described playback
     */
    public PlaybackDetailsDto(Release release, Recording recording) {
        this.release = release;
        this.recording = recording;
    }

    public Reference<Release> getReleaseReference() {
        return new Reference<>(release);
    }

    public Reference<ReleaseGroup> getReleaseGroupReference() {
        return new Reference<ReleaseGroup>(release.getReleaseGroup());
    }

    public Reference<Recording> getRecordingIdentifier() {
        return new Reference<>(recording);
    }

    public List<Reference<Artist>> getRecordingArtistReferences() {
        return recording.getArtistCredit().getArtistCreditName().stream()
                .map(ArtistCreditName::getArtist).map(Reference::new)
                .collect(Collectors.toList());
    }

    public List<Reference<Artist>> getReleaseGroupArtistReferences() {
        return release.getReleaseGroup().getArtistCredit().getArtistCreditName().stream()
                .map(ArtistCreditName::getArtist).map(Reference::new)
                .collect(Collectors.toList());
    }
}
