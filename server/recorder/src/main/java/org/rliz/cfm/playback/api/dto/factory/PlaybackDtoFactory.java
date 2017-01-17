package org.rliz.cfm.playback.api.dto.factory;

import org.rliz.cfm.playback.api.dto.PlaybackDto;
import org.rliz.cfm.playback.model.Playback;
import org.springframework.stereotype.Controller;

/**
 * Creates {@link PlaybackDto}s.
 */
@Controller
public class PlaybackDtoFactory {

    public PlaybackDto build(Playback playback) {
        PlaybackDto dto = new PlaybackDto(playback);
        return dto;
    }
}
