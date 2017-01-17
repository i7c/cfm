package org.rliz.cfm.playback.api.dto;

import org.rliz.cfm.common.api.dto.AbstractDto;
import org.rliz.cfm.playback.model.Playback;

import java.util.Date;

/**
 * Represents a {@link Playback} on the wire.
 */
public class PlaybackDto extends AbstractDto<Playback> {

    public PlaybackDto(Playback data) {
        super(data);
    }

    public Date getTime() {
        return data.getTime();
    }

}
