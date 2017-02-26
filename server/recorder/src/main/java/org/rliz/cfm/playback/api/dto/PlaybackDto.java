package org.rliz.cfm.playback.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rliz.cfm.common.api.dto.AbstractDto;
import org.rliz.cfm.common.api.dto.Reference;
import org.rliz.cfm.playback.model.Playback;
import org.rliz.cfm.recording.api.dto.RecordingDto;
import org.rliz.cfm.release.api.dto.ReleaseGroupDto;
import org.rliz.cfm.user.model.User;

import java.util.Date;
import java.util.List;

/**
 * Represents a {@link Playback} on the wire.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaybackDto extends AbstractDto<Playback> {

    private RecordingDto recording;

    private ReleaseGroupDto releaseGroup;

    public PlaybackDto(Playback data, RecordingDto recordingDto, ReleaseGroupDto releaseGroupDto) {
        super(data);
        this.recording = recordingDto;
        this.releaseGroup = releaseGroupDto;
    }

    public Date getTime() {
        return data.getTime();
    }

    public RecordingDto getRecording() {
        return recording;
    }

    public ReleaseGroupDto getReleaseGroup() {
        return releaseGroup;
    }

    public Reference<User> getUserRef() {
        return new Reference<>(data.getUser());
    }

    public String getOriginalTitle() {
        return data.getOriginalTitle();
    }

    public String getOriginalAlbum() {
        return data.getOriginalAlbum();
    }

    public List<String> getOriginalArtists() {
        return data.getOriginalArtists();
    }

    public Long getPlayTime() {
        return data.getPlayTime();
    }
}
