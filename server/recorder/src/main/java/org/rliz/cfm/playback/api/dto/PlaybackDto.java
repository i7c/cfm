package org.rliz.cfm.playback.api.dto;

import org.rliz.cfm.common.api.dto.AbstractDto;
import org.rliz.cfm.playback.model.Playback;
import org.rliz.cfm.recording.api.dto.RecordingDto;

import java.util.Date;

/**
 * Represents a {@link Playback} on the wire.
 */
public class PlaybackDto extends AbstractDto<Playback> {

    private RecordingDto recording;

    public PlaybackDto(Playback data, RecordingDto recording) {
        super(data);
        this.recording = recording;
    }

    public Date getTime() {
        return data.getTime();
    }

    public RecordingDto getRecording() {
        return recording;
    }
}
