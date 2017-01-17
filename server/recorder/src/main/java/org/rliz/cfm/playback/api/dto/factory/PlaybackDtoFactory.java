package org.rliz.cfm.playback.api.dto.factory;

import org.rliz.cfm.playback.api.dto.PlaybackDto;
import org.rliz.cfm.playback.model.Playback;
import org.rliz.cfm.recording.api.dto.RecordingDto;
import org.rliz.cfm.recording.api.dto.factory.RecordingDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Creates {@link PlaybackDto}s.
 */
@Controller
public class PlaybackDtoFactory {

    private RecordingDtoFactory recordingDtoFactory;

    @Autowired
    public PlaybackDtoFactory(RecordingDtoFactory recordingDtoFactory) {
        this.recordingDtoFactory = recordingDtoFactory;
    }

    public PlaybackDto build(Playback playback) {
        RecordingDto recordingDto  = recordingDtoFactory.build(playback.getRecording());
        PlaybackDto dto = new PlaybackDto(playback, recordingDto);
        return dto;
    }
}
