package org.rliz.cfm.playback.api.dto.factory;

import org.rliz.cfm.playback.api.dto.PlaybackDto;
import org.rliz.cfm.playback.model.Playback;
import org.rliz.cfm.recording.api.dto.RecordingDto;
import org.rliz.cfm.recording.api.dto.factory.RecordingDtoFactory;
import org.rliz.cfm.release.api.dto.ReleaseGroupDto;
import org.rliz.cfm.release.api.dto.factory.ReleaseGroupDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Creates {@link PlaybackDto}s.
 */
@Controller
public class PlaybackDtoFactory {

    private RecordingDtoFactory recordingDtoFactory;

    private ReleaseGroupDtoFactory releaseGroupDtoFactory;


    @Autowired
    public PlaybackDtoFactory(RecordingDtoFactory recordingDtoFactory,
                              ReleaseGroupDtoFactory releaseGroupDtoFactory) {
        this.recordingDtoFactory = recordingDtoFactory;
        this.releaseGroupDtoFactory = releaseGroupDtoFactory;
    }

    public PlaybackDto build(Playback playback) {
        RecordingDto recordingDto = recordingDtoFactory.build(playback.getRecording());
        ReleaseGroupDto releaseGroupDto = releaseGroupDtoFactory.build(playback.getReleaseGroup());
        PlaybackDto dto = new PlaybackDto(playback, recordingDto, releaseGroupDto);
        return dto;
    }
}
